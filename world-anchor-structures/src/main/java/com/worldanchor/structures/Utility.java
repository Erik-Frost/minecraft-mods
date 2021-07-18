package com.worldanchor.structures;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class Utility {

    public static boolean BaseOverworldStoneOrOre(BlockState blockstate) {
        return blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)
                || blockstate.isIn(BlockTags.COAL_ORES) || blockstate.isIn(BlockTags.IRON_ORES)
                || blockstate.isIn(BlockTags.COPPER_ORES) || blockstate.isIn(BlockTags.GOLD_ORES)
                || blockstate.isIn(BlockTags.DIAMOND_ORES) || blockstate.isIn(BlockTags.EMERALD_ORES)
                || blockstate.isIn(BlockTags.REDSTONE_ORES) || blockstate.isIn(BlockTags.LAPIS_ORES);
    }

    public static class PlacementData {
        public BlockPos blockPos;
        public BlockRotation rotation;
        public boolean canPlace;

        public PlacementData(BlockPos blockPos, BlockRotation rotation, boolean canPlace) {
            this.blockPos = blockPos;
            this.rotation = rotation;
            this.canPlace = canPlace;
        }
    }

    public static <FC extends FeatureConfig, S extends StructureFeature<FC>> void registerStructure(Identifier id, S f, GenerationStep.Feature step, int spacing, int separation, int salt,  ConfiguredStructureFeature<FC, ? extends StructureFeature<FC>> c, boolean adjustsSurface) {
        FabricStructureBuilder<FC, S> builder = FabricStructureBuilder.create(id, f)
                .step(step)
                .defaultConfig(spacing, separation, salt)
                .superflatFeature(c);
        if (adjustsSurface) {
            builder.adjustsSurface();
        }
        builder.register();
    }

    public interface BlockStateInterface {
        boolean compareBlockState(BlockState blockstate);
    }

    public static boolean verticalBlockSampleCompare(VerticalBlockSample sample, BlockStateInterface condition,
            int from, int to) {
        if (from < to) {
            for (int y = from; y < to; y++) {
                if (!condition.compareBlockState(sample.getState(new BlockPos(0, y, 0)))) return false;
            }
        }
        else {
            for (int y = from; y > to; y--) {
                if (!condition.compareBlockState(sample.getState(new BlockPos(0, y, 0)))) return false;
            }
        }
        return true;
    }

    public static boolean boxBlockSampleCompare(BlockStateInterface condition, ChunkGenerator chunkGenerator,
            HeightLimitView world, BlockPos corner1, BlockPos corner2) {
        if (corner1.getX() < corner2.getX()) {
            for (int x = corner1.getX(); x < corner2.getX(); x++) {
                if (corner1.getZ() < corner2.getZ()) {
                    for (int z = corner1.getZ(); z < corner2.getZ(); z++) {
                        if (!verticalBlockSampleCompare(chunkGenerator.getColumnSample(x, z, world), condition, corner1.getY(), corner2.getY())) return false;
                    }
                }
                else {
                    for (int z = corner2.getZ(); z < corner1.getZ(); z++) {
                        if (!verticalBlockSampleCompare(chunkGenerator.getColumnSample(x, z, world), condition, corner1.getY(), corner2.getY())) return false;
                    }
                }

            }
        }
        else {
            for (int x = corner1.getX(); x > corner2.getX(); x--) {
                if (corner1.getZ() < corner2.getZ()) {
                    for (int z = corner1.getZ(); z < corner2.getZ(); z++) {
                        if (!verticalBlockSampleCompare(chunkGenerator.getColumnSample(x, z, world), condition, corner1.getY(), corner2.getY())) return false;
                    }
                }
                else {
                    for (int z = corner1.getZ(); z > corner2.getZ(); z--) {
                        if (!verticalBlockSampleCompare(chunkGenerator.getColumnSample(x, z, world), condition, corner1.getY(), corner2.getY())) return false;
                    }
                }

            }
        }
        return true;
    }


    public interface PlacementDataInterface {
        PlacementData getPlacementData(ChunkGenerator chunkGenerator, ChunkRandom random, ChunkPos chunkPos,
                HeightLimitView world);
    }

    public static class Start extends StructureStart<StructurePoolFeatureConfig> {

        public PlacementDataInterface placementDataInterface;

        public Start(StructureFeature<StructurePoolFeatureConfig> feature, ChunkPos pos, int references, long seed,
                PlacementDataInterface placementDataInterface) {
            super(feature, pos, references, seed);
            this.placementDataInterface = placementDataInterface;
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator,
                StructureManager manager, ChunkPos chunkPos, Biome biome, StructurePoolFeatureConfig config,
                HeightLimitView world) {
            // Get placement data
            Utility.PlacementData placementData = placementDataInterface
                    .getPlacementData(chunkGenerator, random, chunkPos, world);
            // Generate structure
            StructurePoolBasedGenerator.generate(registryManager, config,
                    (structureManager, structurePoolElement, pos, groundLevelData, blockRotation, boundingBox) ->
                            new PoolStructurePiece(structureManager, structurePoolElement, pos, groundLevelData,
                            placementData.rotation, structurePoolElement.getBoundingBox(structureManager, pos, placementData.rotation)), chunkGenerator,
                    manager, placementData.blockPos, this, random, false, false, world);
            // Makes bounding box encompass all structure pieces
            setBoundingBoxFromChildren();
        }

    }

}
