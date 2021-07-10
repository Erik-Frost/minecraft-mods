package com.worldanchor.structures;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import static com.worldanchor.structures.Utility.verticalBlockSampleCompare;

public class Utility {

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
        VerticalBlockSample verticalBlockSample;
        if (corner1.getX() < corner2.getX()) {
            for (int x = corner1.getX(); x < corner2.getX(); x++) {
                if (corner1.getZ() < corner2.getZ()) {
                    for (int z = corner1.getZ(); z < corner2.getZ(); z++) {
                        verticalBlockSample = chunkGenerator.getColumnSample(x, z, world);
                        if (!verticalBlockSampleCompare(verticalBlockSample, condition, corner1.getY(), corner2.getY())) return false;
                    }
                }
                else {
                    for (int z = corner2.getZ(); z < corner1.getZ(); z++) {
                        verticalBlockSample = chunkGenerator.getColumnSample(x, z, world);
                        if (!verticalBlockSampleCompare(verticalBlockSample, condition, corner1.getY(), corner2.getY())) return false;
                    }
                }

            }
        }
        else {
            for (int x = corner2.getX(); x < corner1.getX(); x++) {
                if (corner1.getZ() < corner2.getZ()) {
                    for (int z = corner1.getZ(); z < corner2.getZ(); z++) {
                        verticalBlockSample = chunkGenerator.getColumnSample(x, z, world);
                        if (!verticalBlockSampleCompare(verticalBlockSample, condition, corner1.getY(), corner2.getY())) return false;
                    }
                }
                else {
                    for (int z = corner2.getZ(); z < corner1.getZ(); z++) {
                        verticalBlockSample = chunkGenerator.getColumnSample(x, z, world);
                        if (!verticalBlockSampleCompare(verticalBlockSample, condition, corner1.getY(), corner2.getY())) return false;
                    }
                }

            }
        }
        return true;
    }

}
