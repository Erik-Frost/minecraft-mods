package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Server;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.RandomDeleteStructureProcessor;
import com.worldanchor.structures.processors.ReplaceBlocksStructureProcessor;
import com.worldanchor.structures.processors.RuinsStructureProcessor;
import net.minecraft.block.Blocks;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
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

import java.util.ArrayList;
import java.util.Arrays;

import static com.worldanchor.structures.Server.MODID;
import static com.worldanchor.structures.Utility.registerStructure;

public class GiantBeehiveFeature extends StructureFeature<StructurePoolFeatureConfig> {
    public static Identifier ID = new Identifier(MODID + ":giant-beehive");

    public static StructureProcessorList GIANT_BEEHIVE_PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":giant-beehive-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new RandomDeleteStructureProcessor(0.05f, false, Arrays.asList(Blocks.YELLOW_CONCRETE.getDefaultState())),
                            new ReplaceBlocksStructureProcessor(Arrays.asList(Blocks.YELLOW_CONCRETE.getDefaultState()),
                                    Arrays.asList(Pair.of(2, Blocks.YELLOW_CONCRETE.getDefaultState()),
                                            Pair.of(1, Blocks.HONEY_BLOCK.getDefaultState()),
                                            Pair.of(4, Blocks.HONEYCOMB_BLOCK.getDefaultState())
                                    )
                            )
                    )
            )
    );
    public static StructurePool STRUCTURE_POOLS = StructurePools.register(
            new StructurePool(
                    ID, new Identifier("empty"),
                    ImmutableList.of(
                            // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                            Pair.of(StructurePoolElement.ofProcessedSingle(MODID + ":giant-beehive",
                                    GIANT_BEEHIVE_PROCESSOR_LIST), 1)
                    ),
                    StructurePool.Projection.RIGID
            )
    );
    public static final StructureFeature<StructurePoolFeatureConfig> DEFAULT =
            new GiantBeehiveFeature(StructurePoolFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<StructurePoolFeatureConfig,
            ? extends StructureFeature<StructurePoolFeatureConfig>> CONFIGURED
            = DEFAULT.configure(new StructurePoolFeatureConfig(() -> STRUCTURE_POOLS, 1));
    static {
        registerStructure(ID, DEFAULT, GenerationStep.Feature.SURFACE_STRUCTURES,
                64,60,634523774,CONFIGURED, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID, CONFIGURED);
    }


    public GiantBeehiveFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return (feature, pos, references, seed) -> new Utility.Start(feature, pos, references, seed,
                GiantBeehiveFeature::getPlacementData);
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
            for (int y = -50; y < 10; y++) {
                // Check all 4 corner edges from where the base ends to the peak, should all be air blocks
                if (!Utility.boxBlockSampleCompare(Utility::BaseOverworldStoneOrOre, chunkGenerator, world,
                        new BlockPos(x + (8 * xMod), y + 8, z + (8 * zMod)),
                        new BlockPos(x + (18 * xMod), y + 18, z + (18 * zMod)))) continue;
                return new Utility.PlacementData(new BlockPos(x, y, z), rotation, true);
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


