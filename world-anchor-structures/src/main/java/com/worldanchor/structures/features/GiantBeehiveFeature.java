package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.BiomeStructureProcessor;
import com.worldanchor.structures.processors.RandomDeleteStructureProcessor;
import com.worldanchor.structures.processors.ReplaceBlocksStructureProcessor;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.worldanchor.structures.Server.MODID;
import static com.worldanchor.structures.Utility.registerStructure;

public class GiantBeehiveFeature extends Utility.ModStructureFeature {
    public static Identifier ID = new Identifier(MODID + ":giant-beehive");

    public static StructureProcessorList PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":giant-beehive-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new RandomDeleteStructureProcessor(0.05f, false, Arrays.asList(
                                    Blocks.OAK_PLANKS.getDefaultState()
                            )),
                            new ReplaceBlocksStructureProcessor(Arrays.asList(Blocks.YELLOW_CONCRETE.getDefaultState()),
                                    Arrays.asList(new Pair<>("7", Blocks.HONEY_BLOCK.getDefaultState()),
                                            new Pair<>("20", Blocks.HONEYCOMB_BLOCK.getDefaultState()),
                                            new Pair<>("2", Blocks.RAW_GOLD_BLOCK.getDefaultState()),
                                            new Pair<>("1", Blocks.GLOWSTONE.getDefaultState())
                                    )
                            ),
                            new BiomeStructureProcessor(true)
                    )
            )
    );
    public static StructurePool STRUCTURE_POOLS = StructurePools.register(
            new StructurePool(
                    ID, new Identifier("empty"),
                    ImmutableList.of(
                            // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                            Pair.of(StructurePoolElement.ofProcessedSingle(MODID + ":giant-beehive",
                                    PROCESSOR_LIST), 1)
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
        registerStructure(ID, DEFAULT, GenerationStep.Feature.STRONGHOLDS,
                64,60,634523774,CONFIGURED, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID, CONFIGURED);
    }


    public GiantBeehiveFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public @Nullable Utility.PlacementData shouldStartAt(DynamicRegistryManager dynamicRegistryManager,
            ChunkGenerator generator, BiomeSource biomeSource, StructureManager manager, long worldSeed, ChunkPos pos,
            Biome biome, int referenceCount, ChunkRandom random, StructureConfig structureConfig,
            StructurePoolFeatureConfig config, HeightLimitView world, BlockRotation rotation, int xMod, int zMod) {
        int x = pos.getStartX(), z = pos.getStartZ();
        Structure mask = manager.getStructure(new Identifier(MODID + ":" + ID.getPath() + "-mask")).get();
        for (int y = generator.getHeightOnGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, world) - 20;
                y < generator.getHeightOnGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, world); y++) {
            if (Utility.TestStructureMask(generator, world, mask, new BlockPos(x, y, z), xMod, zMod))
                return new Utility.PlacementData(new BlockPos(x, y, z), rotation);
        }
        // Structure can't generate over here, return false
        return null;
    }

}


