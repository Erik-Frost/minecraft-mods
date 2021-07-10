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
import net.minecraft.tag.BlockTags;
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

public class SilverfishNestFeature extends StructureFeature<StructurePoolFeatureConfig> {

    public SilverfishNestFeature(Codec<StructurePoolFeatureConfig> codec) {
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
        VerticalBlockSample mainCornerSample = chunkGenerator.getColumnSample(x + 8, z + 8, world);
        VerticalBlockSample secondCornerSample = chunkGenerator.getColumnSample(x + 18, z + 8, world);
        VerticalBlockSample thirdCornerSample = chunkGenerator.getColumnSample(x + 8, z + 18, world);
        VerticalBlockSample fourthCornerSample = chunkGenerator.getColumnSample(x + 18, z + 18, world);
        for(int y = -50; y < 10; y++) {
            // Check all 4 corner edges from where the base ends to the peak, should all be air blocks
            if (!verticalBlockSampleCompare(mainCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
            else if (!verticalBlockSampleCompare(secondCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
            else if (!verticalBlockSampleCompare(thirdCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
            else if (!verticalBlockSampleCompare(fourthCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
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
            VerticalBlockSample mainCornerSample = chunkGenerator.getColumnSample(x + 8, z + 8, world);
            VerticalBlockSample secondCornerSample = chunkGenerator.getColumnSample(x + 18, z + 8, world);
            VerticalBlockSample thirdCornerSample = chunkGenerator.getColumnSample(x + 8, z + 18, world);
            VerticalBlockSample fourthCornerSample = chunkGenerator.getColumnSample(x + 18, z + 18, world);
            BlockPos mainBlockPos = null;
            for(int y = -50; y < 10; y++) {
                mainBlockPos = new BlockPos(x, y, z);
                // Check all 4 corner edges from where the base ends to the peak, should all be air blocks
                if (!verticalBlockSampleCompare(mainCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
                else if (!verticalBlockSampleCompare(secondCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
                else if (!verticalBlockSampleCompare(thirdCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
                else if (!verticalBlockSampleCompare(fourthCornerSample, (blockstate -> blockstate.isIn(BlockTags.BASE_STONE_OVERWORLD)), y + 8, y + 8 + 10)) continue;
                // Structure can generate here, break
                break;
            }

            boolean randomYPos = false;
            boolean calculateMaxYFromPiecePositions = false;


            StructurePoolBasedGenerator.generate(registryManager, config, SilverfishNestPieces::new, chunkGenerator,
                    manager, mainBlockPos, this, random, calculateMaxYFromPiecePositions, randomYPos, world);

            setBoundingBoxFromChildren();
        }
    }

    public static class SilverfishNestPieces extends PoolStructurePiece {

        public SilverfishNestPieces(StructureManager structureManager,
                StructurePoolElement poolElement, BlockPos pos,
                int groundLevelDelta, BlockRotation rotation, BlockBox boundingBox) {
            super(structureManager, poolElement, pos, groundLevelDelta, rotation, boundingBox);
        }

        public SilverfishNestPieces(ServerWorld world, NbtCompound tag) {
            super(world, tag);
        }

        public static StructurePool STRUCTURE_POOLS = StructurePools.register(
                new StructurePool(
                        new Identifier(MODID + ":silverfish-nest"),
                        new Identifier("empty"),
                        ImmutableList.of(
                                // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                                Pair.of(StructurePoolElement.ofProcessedSingle(MODID + ":silverfish-nest",
                                        Server.SILVERFISH_NEST_PROCESSOR_LIST), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
    }
}


