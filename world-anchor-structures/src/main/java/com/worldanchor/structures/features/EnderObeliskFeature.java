package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Server;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.ChestLootStructureProcessor;
import com.worldanchor.structures.processors.RandomDeleteStructureProcessor;
import com.worldanchor.structures.processors.RuinsStructureProcessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
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
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, ID + "-processor-list", new StructureProcessorList(
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
    public static StructurePool STRUCTURE_POOLS = StructurePools.register(new StructurePool(
            ID, new Identifier("empty"),
            ImmutableList.of(
                    // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                    Pair.of(StructurePoolElement.ofProcessedSingle(ID.toString(), PROCESSOR_LIST), 1)
            ),
            StructurePool.Projection.RIGID
    ));
    public static StructureFeature<StructurePoolFeatureConfig> DEFAULT =
            new EnderObeliskFeature(StructurePoolFeatureConfig.CODEC);
    static {
        registerStructure(ID, DEFAULT, GenerationStep.Feature.STRONGHOLDS,
                124, 110, 120301413, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID,
                DEFAULT.configure(new StructurePoolFeatureConfig(() -> STRUCTURE_POOLS, STRUCTURE_POOLS.getElementCount())));

    }

    public EnderObeliskFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec, ID);
    }

    @Override
    public @Nullable Utility.PlacementData shouldStartAt(ChunkGenerator generator, ChunkPos pos,
            ChunkRandom random, HeightLimitView world, BlockRotation rotation) {
        BlockPos structurePos = TestStructureMask(generator, world,
                Math.min(world.getHeight() - 34, generator.getHeightOnGround(pos.getStartX(), pos.getStartZ(), Heightmap.Type.WORLD_SURFACE_WG, world) + 8),
                generator.getHeightOnGround(pos.getStartX(), pos.getStartZ(), Heightmap.Type.WORLD_SURFACE_WG, world) - 8, -1, rotation, pos);
        if (structurePos == null) return null;
        else return new Utility.PlacementData(structurePos, rotation);

    }

}


