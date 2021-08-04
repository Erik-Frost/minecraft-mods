package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.OresStructureProcessor;
import com.worldanchor.structures.processors.RandomDeleteStructureProcessor;
import com.worldanchor.structures.processors.RuinsStructureProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.worldanchor.structures.Server.MODID;
import static com.worldanchor.structures.Utility.registerStructure;

public class SilverfishNestFeature extends Utility.ModStructureFeature {
    public static ResourceLocation ID = new ResourceLocation(MODID + ":silverfish-nest");

    public static WeightedRandomList<MobSpawnSettings.SpawnerData> MONSTER_SPAWNS = WeightedRandomList.create(new MobSpawnSettings.SpawnerData(
            EntityType.SILVERFISH, 1, 1, 5));

    public static final StructureProcessorList PROCESSOR_LIST = BuiltinRegistries.register(
            BuiltinRegistries.PROCESSOR_LIST, ID + "-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new RandomDeleteStructureProcessor(0.15f, true, Arrays.asList(
                                    Blocks.SPAWNER.defaultBlockState()
                            )),
                            new OresStructureProcessor(true, 0.2f, 0, 0, 0, 0,
                                    0, 0, 1, 0, 0),
                            new RuinsStructureProcessor(0, 0, 0, 0.75F)
                    ))
    );
    public static StructureTemplatePool STRUCTURE_POOLS = Pools.register(new StructureTemplatePool(
            ID, new ResourceLocation("empty"),
            ImmutableList.of(
                    // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                    Pair.of(StructurePoolElement.single(ID.toString(), PROCESSOR_LIST), 1)
            ),
            StructureTemplatePool.Projection.RIGID
    ));
    public static StructureFeature<JigsawConfiguration> DEFAULT =
            new SilverfishNestFeature(JigsawConfiguration.CODEC);
    static {
        registerStructure(ID, DEFAULT, GenerationStep.Decoration.UNDERGROUND_DECORATION,
                32, 16, 502359193, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID,
                DEFAULT.configured(new JigsawConfiguration(() -> STRUCTURE_POOLS, STRUCTURE_POOLS.size())));

    }

    public SilverfishNestFeature(Codec<JigsawConfiguration> codec) {
        super(codec, ID);
    }

    @Override
    public @Nullable Utility.PlacementData shouldStartAt(ChunkGenerator generator, ChunkPos pos,
            WorldgenRandom random, LevelHeightAccessor world, Rotation rotation) {
        int randomY = random.nextInt(70) - 60;
        BlockPos structurePos = TestStructureMask(generator, world, randomY, randomY+1, 1, rotation, pos);
        if (structurePos == null) return null;
        else return new Utility.PlacementData(structurePos, rotation);
    }

}


