package com.worldanchor.structures;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.mixin.StructureTemplateMixin;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawJunction;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.worldanchor.structures.Server.MODID;

public class Utility {

    public static <FC extends FeatureConfiguration, S extends StructureFeature<FC>> void registerStructure(ResourceLocation id, S f,
            GenerationStep.Decoration step, int spacing, int separation, int salt, boolean adjustsSurface) {
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
        public Rotation rotation;
        

        public PlacementData(BlockPos blockPos, Rotation rotation) {
            this.blockPos = blockPos;
            this.rotation = rotation;
        }
    }


    public static class ModStructureStart extends StructureStart<JigsawConfiguration> {

        PlacementData placementData;
        boolean surface;

        public ModStructureStart(StructureFeature<JigsawConfiguration> feature, ChunkPos pos, int references, long seed) {
            super(feature, pos, references, seed);
        }

        public void setVariables(PlacementData placementData, boolean surface) {
            this.placementData = placementData;
            this.surface = surface;
        }

        @Override
        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator,
                StructureManager structureManager, ChunkPos chunkPos, Biome biome,
                JigsawConfiguration featureConfiguration, LevelHeightAccessor levelHeightAccessor) {
            // Generate structure
            JigsawPlacement.addPieces(registryAccess, featureConfiguration,
                    (manager, structurePoolElement, pos, groundLevelData, Rotation, boundingBox) ->
                            new PoolElementStructurePiece(manager, structurePoolElement, pos, groundLevelData,
                                    placementData.rotation, structurePoolElement.getBoundingBox(manager, pos, placementData.rotation)), chunkGenerator,
                    structureManager, placementData.blockPos, this, random, false, surface, levelHeightAccessor);
            // Makes bounding box encompass all structure pieces
            createBoundingBox();
        }


    }



    public abstract static class ModStructureFeature extends StructureFeature<JigsawConfiguration> {

        public Map<Pair<Integer, Integer>, ArrayList<StructureTemplate.StructureBlockInfo>> maskBlockInfoList;
        public final ResourceLocation id;
        

        public ModStructureFeature(Codec<JigsawConfiguration> codec, ResourceLocation id) {
            super(codec);
            this.id = id;
        }


        @Override
        public StructureStartFactory<JigsawConfiguration> getStartFactory() {
            return ModStructureStart::new;
        }

        @Override
        public StructureStart<?> generate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator,
                BiomeSource biomeSource, StructureManager structureManager, long l, ChunkPos chunkPos, Biome biome,
                int i, WorldgenRandom worldgenRandom, StructureFeatureConfiguration structureFeatureConfiguration,
                JigsawConfiguration featureConfiguration, LevelHeightAccessor levelHeightAccessor) {
            ChunkPos potentialChunkPos = this.getPotentialFeatureChunk(structureFeatureConfiguration, l, worldgenRandom, chunkPos.x, chunkPos.z);
            PlacementData placementData;
            if (chunkPos.x == potentialChunkPos.x && chunkPos.z == potentialChunkPos.z) {
                if (maskBlockInfoList == null) {
                    maskBlockInfoList = new HashMap<>();
                    for (StructureTemplate.Palette palettedBlockInfoList :
                            ((StructureTemplateMixin) structureManager.get(new ResourceLocation(MODID + ":" + id.getPath() + "-mask")).get()).getPalettes()) {
                        for (StructureTemplate.StructureBlockInfo maskBlockInfo : palettedBlockInfoList.blocks()) {
                            if (!maskBlockInfo.state.is(Blocks.STRUCTURE_VOID)) {
                                Pair<Integer, Integer> coords = new Pair<>(maskBlockInfo.pos.getX(), maskBlockInfo.pos.getZ());
                                if (!maskBlockInfoList.containsKey(coords)) maskBlockInfoList.put(coords, new ArrayList<>());
                                maskBlockInfoList.get(coords).add(maskBlockInfo);
                            }
                        }
                    }
                }
                for (Rotation rotation : Rotation.getShuffled(worldgenRandom)) {
                    if ((placementData = shouldStartAt(chunkGenerator, chunkPos, worldgenRandom, levelHeightAccessor, rotation)) != null) {
                        ModStructureStart structureModStructureStart = (ModStructureStart) getStartFactory().create(this, chunkPos, i, l);
                        structureModStructureStart.setVariables(placementData, false);
                        structureModStructureStart.generatePieces(registryAccess, chunkGenerator, structureManager, chunkPos, biome, featureConfiguration, levelHeightAccessor);
                        if (structureModStructureStart.isValid()) {
                            return structureModStructureStart;
                        }
                    }
                }
            }

            return StructureStart.INVALID_START;
        }


        @Nullable
        public abstract PlacementData shouldStartAt(ChunkGenerator generator, ChunkPos pos,
                WorldgenRandom worldgenRandom, LevelHeightAccessor world, Rotation rotation);


        public BlockPos TestStructureMask(ChunkGenerator generator, LevelHeightAccessor world,
                int yFrom, int yTo, int yIncrement, Rotation rotation, ChunkPos chunkPos) {
            int startX = chunkPos.getMinBlockX(), startZ = chunkPos.getMinBlockZ();
            Map<Pair<Integer, Integer>, NoiseColumn> noiseColumnHashMap = new HashMap<>();
            Pair<Integer, Integer> rotatedKey;
            boolean validPosition;
            for (int y = yFrom; y != yTo; y += yIncrement) {
                validPosition = true;
                for (Pair<Integer, Integer> key : maskBlockInfoList.keySet()) {
                    // This rotation logic needs to match that in Structure.transformAround
                    rotatedKey = key;
                    if (rotation == Rotation.COUNTERCLOCKWISE_90) rotatedKey = new Pair<>(startX - startZ + (key.getSecond() + startZ), startX + startZ - (key.getFirst() + startX));
                    else if (rotation == Rotation.CLOCKWISE_90) rotatedKey = new Pair<>(startX + startZ - (key.getSecond() + startZ), startZ - startX + (key.getFirst() + startX));
                    else if (rotation == Rotation.CLOCKWISE_180) rotatedKey = new Pair<>(startX + startX - (key.getFirst() + startX), startZ + startZ - (key.getSecond() + startZ));
                    if (!noiseColumnHashMap.containsKey(rotatedKey)) noiseColumnHashMap.put(rotatedKey,
                            generator.getBaseColumn(rotatedKey.getFirst(), rotatedKey.getSecond(), world));
                    for (StructureTemplate.StructureBlockInfo maskBlockInfo : maskBlockInfoList.get(key)) {
                        BlockState worldBlockState = noiseColumnHashMap.get(rotatedKey).getBlockState(new BlockPos(0, maskBlockInfo.pos.getY() + y, 0));
                        if ((maskBlockInfo.state.isAir() && !worldBlockState.isAir())
                                || (maskBlockInfo.state.is(Blocks.BEDROCK) && (worldBlockState.isAir() || worldBlockState.is(Blocks.WATER) || worldBlockState.is(Blocks.LAVA)))
                                || (maskBlockInfo.state.is(Blocks.LAVA) && !worldBlockState.is(Blocks.LAVA))
                                || (maskBlockInfo.state.is(Blocks.WATER) && !worldBlockState.is(Blocks.WATER))) {
                            validPosition = false;
                            break;
                        }
                    }
                    if (!validPosition) break;
                }
                if (validPosition) return new BlockPos(startX, y+1, startZ);
            }
            return null;
        }
    }
}
