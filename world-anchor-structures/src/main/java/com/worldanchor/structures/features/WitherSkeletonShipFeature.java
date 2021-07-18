package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.RandomDeleteStructureProcessor;
import com.worldanchor.structures.processors.RuinsStructureProcessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Arrays;

import static com.worldanchor.structures.Server.MODID;
import static com.worldanchor.structures.Utility.registerStructure;

public class WitherSkeletonShipFeature extends StructureFeature<StructurePoolFeatureConfig> {
    public static Identifier ID = new Identifier(MODID + ":wither-skeleton-ship");

    public static final StructureProcessorList WITHER_SKELETON_SHIP_PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":wither-skeleton-ship-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new RandomDeleteStructureProcessor(0.05f, true, Arrays.asList(
                                    Blocks.SPAWNER.getDefaultState(), Blocks.LAVA.getDefaultState(),
                                    Blocks.CHEST.getDefaultState(), Blocks.WITHER_SKELETON_SKULL.getDefaultState(),
                                    Blocks.BONE_BLOCK.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(),
                                    Blocks.NETHER_BRICK_WALL.getDefaultState(), Blocks.NETHER_BRICK_STAIRS.getDefaultState(),
                                    Blocks.SOUL_SAND.getDefaultState()
                            )),
                            new RuinsStructureProcessor(0.10F, 0.0f, 0.3F,0.0F)
                    ))
    );
    public static StructurePool STRUCTURE_POOLS = StructurePools.register(
            new StructurePool(
                    ID, new Identifier("empty"),
                    ImmutableList.of(
                            // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                            Pair.of(StructurePoolElement.ofProcessedSingle(MODID + ":wither-skeleton-ship",
                                    WITHER_SKELETON_SHIP_PROCESSOR_LIST), 1)
                    ),
                    StructurePool.Projection.RIGID
            )
    );
    public static final StructureFeature<StructurePoolFeatureConfig> DEFAULT =
            new WitherSkeletonShipFeature(StructurePoolFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<StructurePoolFeatureConfig,
            ? extends StructureFeature<StructurePoolFeatureConfig>> CONFIGURED
            = DEFAULT.configure(new StructurePoolFeatureConfig(() -> STRUCTURE_POOLS, 1));
    static {
        registerStructure(ID, DEFAULT, GenerationStep.Feature.VEGETAL_DECORATION,
                48, 32, 858204813, CONFIGURED, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID, CONFIGURED);
    }


    public WitherSkeletonShipFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return (feature, pos, references, seed) -> new Utility.Start(feature, pos, references, seed,
                WitherSkeletonShipFeature::getPlacementData);
    }

    public static Utility.PlacementData getPlacementData(ChunkGenerator chunkGenerator, ChunkRandom random, ChunkPos chunkPos,
            HeightLimitView world) {
        int x = chunkPos.getStartX();
        int z = chunkPos.getStartZ();
        for (BlockRotation rotation : BlockRotation.randomRotationOrder(random)) {
            int xMod = 1,zMod = 1;
            if (rotation == BlockRotation.CLOCKWISE_90) xMod = -1;
            else if (rotation == BlockRotation.CLOCKWISE_180) xMod = zMod = -1;
            else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) zMod = -1;
            if (Utility.boxBlockSampleCompare(blockstate -> blockstate.isOf(Blocks.LAVA), chunkGenerator, world,
                    new BlockPos(x, 29, z), new BlockPos(x + (48 * xMod), 32, z + (19 * zMod)))
                    && Utility.boxBlockSampleCompare(AbstractBlock.AbstractBlockState::isAir, chunkGenerator, world,
                    new BlockPos(x, 33, z), new BlockPos(x +  (48 * xMod), 58, z + (19 * zMod)))) {
                return new Utility.PlacementData(new BlockPos(x, 29, z), rotation, true);
            }
        }
        // Structure can't generate over here, return false
        return new Utility.PlacementData(null, null, false);
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed,
            ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, StructurePoolFeatureConfig config,
            HeightLimitView world) {
        return getPlacementData(chunkGenerator, random, chunkPos, world).canPlace;
    }

}


