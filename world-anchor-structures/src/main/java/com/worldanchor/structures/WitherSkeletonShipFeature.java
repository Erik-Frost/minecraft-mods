package com.worldanchor.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
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

public class WitherSkeletonShipFeature extends StructureFeature<StructurePoolFeatureConfig> {

    public WitherSkeletonShipFeature(Codec<StructurePoolFeatureConfig> codec) {
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
        return Utility.boxBlockSampleCompare(blockstate -> blockstate.isOf(Blocks.LAVA), chunkGenerator, world,
                new BlockPos(x, 29, z), new BlockPos(x + 48, 32, z + 48))
                && Utility.boxBlockSampleCompare(AbstractBlock.AbstractBlockState::isAir, chunkGenerator, world,
                new BlockPos(x, 33, z), new BlockPos(x + 48, 58, z + 48));
    }


    public static class Start extends StructureStart<StructurePoolFeatureConfig> {

        public Start(StructureFeature<StructurePoolFeatureConfig> feature, ChunkPos pos, int references, long seed) {
            super(feature, pos, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator,
                StructureManager manager, ChunkPos chunkPos, Biome biome, StructurePoolFeatureConfig config,
                HeightLimitView world) {

            boolean randomYPos = false;
            boolean calculateMaxYFromPiecePositions = false;

            StructurePoolBasedGenerator.generate(registryManager, config, WitherSkeletonShipPieces::new, chunkGenerator,
                    manager, new BlockPos(chunkPos.getStartX(), 29, chunkPos.getStartZ()), this, random,
                    calculateMaxYFromPiecePositions, randomYPos, world);

            setBoundingBoxFromChildren();
        }
    }

    public static class WitherSkeletonShipPieces extends PoolStructurePiece {

        public WitherSkeletonShipPieces(StructureManager structureManager,
                StructurePoolElement poolElement, BlockPos pos,
                int groundLevelDelta, BlockRotation rotation, BlockBox boundingBox) {
            super(structureManager, poolElement, pos, groundLevelDelta, rotation, boundingBox);
        }

        public WitherSkeletonShipPieces(ServerWorld world, NbtCompound tag) {
            super(world, tag);
        }

        public static StructurePool STRUCTURE_POOLS = StructurePools.register(
                new StructurePool(
                        new Identifier(MODID + ":wither-skeleton-ship"),
                        new Identifier("empty"),
                        ImmutableList.of(
                                // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                                Pair.of(StructurePoolElement.ofProcessedSingle(MODID + ":wither-skeleton-ship",
                                        Server.WITHER_SKELETON_SHIP_PROCESSOR_LIST), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
    }
}


