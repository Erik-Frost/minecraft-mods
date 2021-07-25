package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Server;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.ChestLootStructureProcessor;
import com.worldanchor.structures.processors.RandomDeleteStructureProcessor;
import com.worldanchor.structures.processors.RuinsStructureProcessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
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

public class EnderObeliskFeature extends Utility.ModStructureFeature {
    public static Identifier ID = new Identifier(MODID + ":ender-obelisk");

    public static Pool<SpawnSettings.SpawnEntry> MONSTER_SPAWNS = Pool.of(new SpawnSettings.SpawnEntry(
            EntityType.ENDERMAN, 1, 1, 3));

    public static StructureProcessorList PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":ender-obelisk-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new RandomDeleteStructureProcessor(0.05f, true, Arrays.asList(
                                    Blocks.SPAWNER.getDefaultState(), Blocks.END_STONE.getDefaultState(),
                                    Blocks.OBSIDIAN.getDefaultState(), Blocks.LAVA.getDefaultState(),
                                    Blocks.END_STONE_BRICK_STAIRS.getDefaultState(), Blocks.END_STONE_BRICK_WALL.getDefaultState(),
                                    Blocks.WATER.getDefaultState(), Blocks.CARVED_PUMPKIN.getDefaultState(),
                                    Blocks.JACK_O_LANTERN.getDefaultState(), Blocks.CHEST.getDefaultState()
                            )),
                            new ChestLootStructureProcessor(ID.getPath()),
                            new RuinsStructureProcessor(0.1F, 0.4F, 0.2F,0.3F)
                    ))
    );
    public static StructurePool STRUCTURE_POOLS = StructurePools.register(
            new StructurePool(
                    ID, new Identifier("empty"),
                    ImmutableList.of(
                            // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                            Pair.of(StructurePoolElement.ofProcessedSingle(Server.MODID + ":ender-obelisk",
                                    PROCESSOR_LIST), 1)
                    ),
                    StructurePool.Projection.RIGID
            )
    );
    public static StructureFeature<StructurePoolFeatureConfig> DEFAULT =
            new EnderObeliskFeature(StructurePoolFeatureConfig.CODEC);
    public static ConfiguredStructureFeature<StructurePoolFeatureConfig,
            ? extends StructureFeature<StructurePoolFeatureConfig>> CONFIGURED
            = DEFAULT.configure(new StructurePoolFeatureConfig(() -> STRUCTURE_POOLS, 1));
    static {
        registerStructure(ID, DEFAULT, GenerationStep.Feature.STRONGHOLDS,
                124, 110, 120301413, CONFIGURED, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID, CONFIGURED);

    }

    public EnderObeliskFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public @Nullable Utility.PlacementData shouldStartAt(DynamicRegistryManager dynamicRegistryManager,
            ChunkGenerator generator, BiomeSource biomeSource, StructureManager manager, long worldSeed, ChunkPos pos,
            Biome biome, int referenceCount, ChunkRandom random, StructureConfig structureConfig,
            StructurePoolFeatureConfig config, HeightLimitView world) {
        int x = pos.getStartX();
        int z = pos.getStartZ();
        for (BlockRotation rotation : BlockRotation.randomRotationOrder(random)) {
            int xMod = 1,zMod = 1;
            if (rotation == BlockRotation.CLOCKWISE_90) xMod = -1;
            else if (rotation == BlockRotation.CLOCKWISE_180) xMod = zMod = -1;
            else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) zMod = -1;
            for(int y = generator.getHeightOnGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, world) - 4;
                    y < Math.min(world.getHeight() - 34, generator.getHeightOnGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, world) + 4) ; y++) {
                if (Utility.boxBlockSampleCompare((blockstate) -> !blockstate.isAir() && !blockstate.isOf(
                        Blocks.WATER), generator, world,
                        new BlockPos(x + (2 * xMod), y - 2, z + (2 * zMod)),
                        new BlockPos(x + (11 * xMod), y + 3, z + (11 * zMod)))
                        && Utility.boxBlockSampleCompare(AbstractBlock.AbstractBlockState::isAir, generator, world,
                        new BlockPos(x, y + 4, z), new BlockPos(x + (13 * xMod), y + 34, z + (13 * zMod)))) {
                    return new Utility.PlacementData(new BlockPos(x, y, z), rotation);
                }
            }
        }
        // Structure can't generate over here, return false
        return null;
    }

}

