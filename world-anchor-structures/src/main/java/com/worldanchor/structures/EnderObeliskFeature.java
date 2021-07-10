package com.worldanchor.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import static com.worldanchor.structures.Server.MODID;
import static com.worldanchor.structures.Utility.verticalBlockSampleCompare;

public class EnderObeliskFeature extends StructureFeature<StructurePoolFeatureConfig> {

    public EnderObeliskFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }


    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed,
            ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, StructurePoolFeatureConfig config,
            HeightLimitView world) {
        int x = chunkPos.getStartX();
        int z = chunkPos.getStartZ();
        VerticalBlockSample mainCornerSample = chunkGenerator.getColumnSample(x, z, world);
        VerticalBlockSample secondCornerSample = chunkGenerator.getColumnSample(x + 13, z, world);
        VerticalBlockSample thirdCornerSample = chunkGenerator.getColumnSample(x, z + 13, world);
        VerticalBlockSample fourthCornerSample = chunkGenerator.getColumnSample(x + 13, z + 13, world);
        BlockPos mainBlockPos, secondBlockPos, thirdBlockPos, fourthBlockPos;
        for(int y = -60; y < -15; y++) {
            mainBlockPos = new BlockPos(x, y, z);
            secondBlockPos = new BlockPos(x + 13, y, z);
            thirdBlockPos = new BlockPos(x, y, z + 13);
            fourthBlockPos = new BlockPos(x + 13, y, z + 13);
            // Check all 4 bottom corners, they should be fullopaque blocks
            if (!mainCornerSample.getState(mainBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, mainBlockPos)) continue;
            else if (!secondCornerSample.getState(secondBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, secondBlockPos)) continue;
            else if (!thirdCornerSample.getState(thirdBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, thirdBlockPos)) continue;
            else if (!fourthCornerSample.getState(fourthBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, fourthBlockPos)) continue;
            // Check all 4 corner edges from where the base ends to the peak, should all be air blocks
            if (!verticalBlockSampleCompare(mainCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
            else if (!verticalBlockSampleCompare(secondCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
            else if (!verticalBlockSampleCompare(thirdCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
            else if (!verticalBlockSampleCompare(fourthCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
            // Structure can generate here, return true
            return true;
        }
        // Structure can't generate over here, return false
        return false;
    }


    public static class Start extends StructureStart<StructurePoolFeatureConfig> {

        public Start(StructureFeature<StructurePoolFeatureConfig> feature, ChunkPos pos, int references, long seed) {
            super(feature, pos, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator,
                StructureManager manager, ChunkPos chunkPos, Biome biome, StructurePoolFeatureConfig config,
                HeightLimitView world) {
            int x = chunkPos.getStartX();
            int z = chunkPos.getStartZ();
            VerticalBlockSample mainCornerSample = chunkGenerator.getColumnSample(x, z, world);
            VerticalBlockSample secondCornerSample = chunkGenerator.getColumnSample(x + 13, z, world);
            VerticalBlockSample thirdCornerSample = chunkGenerator.getColumnSample(x, z + 13, world);
            VerticalBlockSample fourthCornerSample = chunkGenerator.getColumnSample(x + 13, z + 13, world);
            BlockPos mainBlockPos = null, secondBlockPos, thirdBlockPos, fourthBlockPos;
            for(int y = -60; y < -15; y++) {
                mainBlockPos = new BlockPos(x, y, z);
                secondBlockPos = new BlockPos(x + 13, y, z);
                thirdBlockPos = new BlockPos(x, y, z + 13);
                fourthBlockPos = new BlockPos(x + 13, y, z + 13);
                // Check all 4 bottom corners, they should be fullopaque blocks
                if (!mainCornerSample.getState(mainBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, mainBlockPos)) continue;
                else if (!secondCornerSample.getState(secondBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, secondBlockPos)) continue;
                else if (!thirdCornerSample.getState(thirdBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, thirdBlockPos)) continue;
                else if (!fourthCornerSample.getState(fourthBlockPos).isOpaqueFullCube(EmptyBlockView.INSTANCE, fourthBlockPos)) continue;
                // Check all 4 corner edges from where the base ends to the peak, should all be air blocks
                if (!verticalBlockSampleCompare(mainCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
                else if (!verticalBlockSampleCompare(secondCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
                else if (!verticalBlockSampleCompare(thirdCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
                else if (!verticalBlockSampleCompare(fourthCornerSample, AbstractBlock.AbstractBlockState::isAir, y + 6, y + 6 + 30)) continue;
                // Structure can generate here, break
                break;
            }

            boolean randomYPos = false;
            boolean calculateMaxYFromPiecePositions = false;

            StructurePoolBasedGenerator.generate(registryManager, config, EnderObeliskPieces::new, chunkGenerator,
                    manager, mainBlockPos, this, random, calculateMaxYFromPiecePositions, randomYPos, world);

            setBoundingBoxFromChildren();
        }
    }

    public static class EnderObeliskPieces extends PoolStructurePiece {

        public EnderObeliskPieces(StructureManager structureManager,
                StructurePoolElement poolElement, BlockPos pos,
                int groundLevelDelta, BlockRotation rotation, BlockBox boundingBox) {
            super(structureManager, poolElement, pos, groundLevelDelta, BlockRotation.NONE, boundingBox);
        }

        public EnderObeliskPieces(ServerWorld world, NbtCompound tag) {
            super(world, tag);
        }

        public static StructurePool STRUCTURE_POOLS = StructurePools.register(
                new StructurePool(
                        new Identifier(MODID + ":ender-obelisk"),
                        new Identifier("empty"),
                        ImmutableList.of(
                                // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                                Pair.of(StructurePoolElement.ofProcessedSingle(Server.MODID + ":ender-obelisk",
                                        Server.ENDER_OBELISK_PROCESSOR_LIST), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
    }
}


