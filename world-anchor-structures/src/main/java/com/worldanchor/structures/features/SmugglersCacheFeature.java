package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.ChestLootStructureProcessor;
import com.worldanchor.structures.processors.RuinsStructureProcessor;
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
import net.minecraft.world.Heightmap;
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

public class SmugglersCacheFeature extends Utility.ModStructureFeature {
    public static Identifier ID = new Identifier(MODID + ":smugglers-cache");

    public static StructureProcessorList PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, ID + "-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new ChestLootStructureProcessor(ID.getPath()),
                            new RuinsStructureProcessor(0.1F, 0.4F, 0.1F,0F)
                    )
            )
    );
    public static StructurePool STRUCTURE_POOLS = StructurePools.register(new StructurePool(
            ID, new Identifier("empty"),
            ImmutableList.of(
                    // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                    Pair.of(StructurePoolElement.ofProcessedSingle(ID.toString(), PROCESSOR_LIST), 1)
            ),
            StructurePool.Projection.RIGID
    ));
    public static final StructureFeature<StructurePoolFeatureConfig> DEFAULT =
            new SmugglersCacheFeature(StructurePoolFeatureConfig.CODEC);
    static {
        StructurePools.register(new StructurePool(
                new Identifier(MODID + ":entity/undead-horse"), new Identifier("empty"),
                ImmutableList.of(
                        // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                        Pair.of(StructurePoolElement.ofSingle(MODID + ":entity/skeleton-horse"), 1),
                        Pair.of(StructurePoolElement.ofSingle(MODID + ":entity/zombie-horse"), 1)
                ),
                StructurePool.Projection.RIGID
        ));
        registerStructure(ID, DEFAULT, GenerationStep.Feature.STRONGHOLDS,
                70,54,568234126, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID,
                DEFAULT.configure(new StructurePoolFeatureConfig(() -> STRUCTURE_POOLS, STRUCTURE_POOLS.getElementCount())));
    }

    public SmugglersCacheFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec, ID);
    }

    @Override
    public @Nullable Utility.PlacementData shouldStartAt(ChunkGenerator generator, ChunkPos pos,
            ChunkRandom random, HeightLimitView world, BlockRotation rotation) {
        BlockPos structurePos = TestStructureMask(generator, world,
                generator.getHeightOnGround(pos.getStartX(), pos.getStartZ(), Heightmap.Type.WORLD_SURFACE_WG, world) - 40,
                generator.getHeightOnGround(pos.getStartX(), pos.getStartZ(), Heightmap.Type.WORLD_SURFACE_WG, world) + 10, 1, rotation, pos);
        if (structurePos == null) return null;
        else return new Utility.PlacementData(structurePos, rotation);
    }

}


