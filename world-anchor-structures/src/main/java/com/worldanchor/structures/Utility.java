package com.worldanchor.structures;

import com.mojang.serialization.Codec;
import com.worldanchor.structures.mixin.StructureMixin;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.worldanchor.structures.Server.MODID;
import static net.minecraft.structure.Structure.transformAround;

public class Utility {

    public static <FC extends FeatureConfig, S extends StructureFeature<FC>> void registerStructure(Identifier id, S f,
            GenerationStep.Feature step, int spacing, int separation, int salt, boolean adjustsSurface) {
        FabricStructureBuilder<FC, S> builder = FabricStructureBuilder.create(id, f)
                .step(step)
                .defaultConfig(spacing, separation, salt);
        if (adjustsSurface) {
            builder.adjustsSurface();
        }
        builder.register();
    }


    public static class PlacementData {
        public BlockPos blockPos;
        public BlockRotation rotation;

        public PlacementData(BlockPos blockPos, BlockRotation rotation) {
            this.blockPos = blockPos;
            this.rotation = rotation;
        }
    }

    public static class ModStructureStart extends StructureStart<StructurePoolFeatureConfig> {

        PlacementData placementData;
        boolean surface;

        public ModStructureStart(StructureFeature<StructurePoolFeatureConfig> feature, ChunkPos pos, int references, long seed) {
            super(feature, pos, references, seed);
        }

        public void setVariables(PlacementData placementData, boolean surface) {
            this.placementData = placementData;
            this.surface = surface;
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator,
                StructureManager manager, ChunkPos chunkPos, Biome biome, StructurePoolFeatureConfig config,
                HeightLimitView world) {
            // Generate structure
            StructurePoolBasedGenerator.generate(registryManager, config,
                    (structureManager, structurePoolElement, pos, groundLevelData, blockRotation, boundingBox) ->
                            new PoolStructurePiece(structureManager, structurePoolElement, pos, groundLevelData,
                                    placementData.rotation, structurePoolElement.getBoundingBox(structureManager, pos, placementData.rotation)), chunkGenerator,
                    manager, placementData.blockPos, this, random, false, surface, world);
            // Makes bounding box encompass all structure pieces
            setBoundingBoxFromChildren();
        }
    }













    public abstract static class ModStructureFeature extends StructureFeature<StructurePoolFeatureConfig> {

        public Map<Pair<Integer, Integer>, ArrayList<Structure.StructureBlockInfo>> maskBlockInfoList;
        public final Identifier id;

        public ModStructureFeature(Codec<StructurePoolFeatureConfig> codec, Identifier id) {
            super(codec);
            this.id = id;
        }



        @Override
        public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
            return ModStructureStart::new;
        }

        @Override
        public StructureStart<?> tryPlaceStart(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator generator,
                BiomeSource biomeSource, StructureManager manager, long worldSeed, ChunkPos pos, Biome biome,
                int referenceCount, ChunkRandom random, StructureConfig structureConfig, StructurePoolFeatureConfig config,
                HeightLimitView world) {
            ChunkPos chunkPos = this.getStartChunk(structureConfig, worldSeed, random, pos.x, pos.z);
            PlacementData placementData;
            if (pos.x == chunkPos.x && pos.z == chunkPos.z) {
                if (maskBlockInfoList == null) {
                    maskBlockInfoList = new HashMap<>();
                    for (Structure.PalettedBlockInfoList palettedBlockInfoList :
                            ((StructureMixin) manager.getStructure(new Identifier(MODID + ":" + id.getPath() + "-mask")).get()).getBlockInfoLists()) {
                        for (Structure.StructureBlockInfo maskBlockInfo : palettedBlockInfoList.getAll()) {
                            if (!maskBlockInfo.state.isOf(Blocks.STRUCTURE_VOID)) {
                                Pair<Integer, Integer> coords = new Pair<>(maskBlockInfo.pos.getX(), maskBlockInfo.pos.getZ());
                                if (!maskBlockInfoList.containsKey(coords)) maskBlockInfoList.put(coords, new ArrayList<>());
                                maskBlockInfoList.get(coords).add(maskBlockInfo);
                            }
                        }
                    }
                }
                for (BlockRotation rotation : BlockRotation.randomRotationOrder(random)) {
                    int xMod = 1,zMod = 1;
                    if (rotation == BlockRotation.CLOCKWISE_90) zMod = -1;
                    else if (rotation == BlockRotation.CLOCKWISE_180) xMod = zMod = -1;
                    else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) xMod = -1;
                    if ((placementData = shouldStartAt(dynamicRegistryManager, generator, biomeSource, manager,
                            worldSeed, pos, biome, referenceCount, random, structureConfig, config, world,
                            rotation, xMod, zMod)) != null) {
                        ModStructureStart structureModStructureStart = (ModStructureStart) this.getStructureStartFactory().create(this, pos, referenceCount, worldSeed);
                        structureModStructureStart.setVariables(placementData, false);
                        structureModStructureStart.init(dynamicRegistryManager, generator, manager, pos, biome, config, world);
                        if (structureModStructureStart.hasChildren()) {
                            return structureModStructureStart;
                        }
                    }
                }
            }

            return StructureStart.DEFAULT;
        }

        @Nullable
        public abstract PlacementData shouldStartAt(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator generator,
                BiomeSource biomeSource, StructureManager manager, long worldSeed, ChunkPos pos, Biome biome,
                int referenceCount, ChunkRandom random, StructureConfig structureConfig, StructurePoolFeatureConfig config,
                HeightLimitView world, BlockRotation rotation, int xMod, int zMod);

        public BlockPos TestStructureMask(ChunkGenerator generator, HeightLimitView world,
                BlockPos structureStartBlockPos, int xMod, int zMod, int yFrom, int yTo, int yIncrement,
                BlockRotation rotation) {
            Map<Pair<Integer, Integer>, VerticalBlockSample> verticalBlockSamples = new HashMap<>();
            Pair<Integer, Integer> rotatedKey;
            BlockPos rotatedBlockPos;
            boolean validPosition;
            for (int y = yFrom; y != yTo; y += yIncrement) {
                validPosition = true;
                for (Pair<Integer, Integer> key : maskBlockInfoList.keySet()) {
                    rotatedBlockPos = transformAround(new BlockPos(key.getLeft() + structureStartBlockPos.getX(), 0, key.getRight() + structureStartBlockPos.getZ()), BlockMirror.NONE, rotation, new BlockPos(structureStartBlockPos.getX(), 0, structureStartBlockPos.getZ()));
                    rotatedKey = new Pair<>(rotatedBlockPos.getX(), rotatedBlockPos.getZ());
                    if (!verticalBlockSamples.containsKey(rotatedKey)) verticalBlockSamples.put(rotatedKey,
                            generator.getColumnSample(rotatedKey.getLeft(), rotatedKey.getRight(), world));
                    for (Structure.StructureBlockInfo maskBlockInfo : maskBlockInfoList.get(key)) {
                        BlockState worldBlockState = verticalBlockSamples.get(rotatedKey).getState(new BlockPos(0, maskBlockInfo.pos.getY() + y, 0));
                        if ((maskBlockInfo.state.isAir() && !worldBlockState.isAir())
                                || (maskBlockInfo.state.isOf(Blocks.BEDROCK) && (worldBlockState.isAir() || worldBlockState.isOf(Blocks.WATER) || worldBlockState.isOf(Blocks.LAVA)))
                                || (maskBlockInfo.state.isOf(Blocks.LAVA) && !worldBlockState.isOf(Blocks.LAVA))
                                || (maskBlockInfo.state.isOf(Blocks.WATER) && !worldBlockState.isOf(Blocks.WATER))) {
                            validPosition = false;
                            break;
                        }
                    }
                    if (!validPosition) break;
                }
                if (validPosition) return new BlockPos(structureStartBlockPos.getX(), y+1, structureStartBlockPos.getZ());
            }
            return null;
        }
    }
}
