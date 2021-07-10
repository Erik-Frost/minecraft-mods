package com.worldanchor.structures;

import com.google.common.collect.ImmutableList;
import com.worldanchor.structures.processors.*;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import net.minecraft.block.Blocks;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;

import java.util.ArrayList;
import java.util.Arrays;

import static com.worldanchor.structures.Utility.registerStructure;

public class Server implements DedicatedServerModInitializer {
    public static final String MODID = "world-anchor-structures";

    // Structure Features
    public static final StructureFeature<StructurePoolFeatureConfig> ENDER_OBELISK_FEATURE =
            new EnderObeliskFeature(StructurePoolFeatureConfig.CODEC);
    public static final StructureFeature<StructurePoolFeatureConfig> SILVERFISH_NEST_FEATURE =
            new SilverfishNestFeature(StructurePoolFeatureConfig.CODEC);
    public static final StructureFeature<StructurePoolFeatureConfig> WITHER_SKELETON_SHIP_FEATURE =
            new WitherSkeletonShipFeature(StructurePoolFeatureConfig.CODEC);

    // Configured Structure Features
    public static final ConfiguredStructureFeature<StructurePoolFeatureConfig,
            ? extends StructureFeature<StructurePoolFeatureConfig>> ENDER_OBELISK_FEATURE_CONFIGURED
            = ENDER_OBELISK_FEATURE.configure(new StructurePoolFeatureConfig(() -> {
                return EnderObeliskFeature.EnderObeliskPieces.STRUCTURE_POOLS; }, 1));
    public static final ConfiguredStructureFeature<StructurePoolFeatureConfig,
            ? extends StructureFeature<StructurePoolFeatureConfig>> SILVERFISH_NEST_FEATURE_CONFIGURED
            = SILVERFISH_NEST_FEATURE.configure(new StructurePoolFeatureConfig(() -> {
        return SilverfishNestFeature.SilverfishNestPieces.STRUCTURE_POOLS; }, 1));
    public static final ConfiguredStructureFeature<StructurePoolFeatureConfig,
            ? extends StructureFeature<StructurePoolFeatureConfig>> WITHER_SKELETON_SHIP_FEATURE_CONFIGURED
            = WITHER_SKELETON_SHIP_FEATURE.configure(new StructurePoolFeatureConfig(() -> {
        return WitherSkeletonShipFeature.WitherSkeletonShipPieces.STRUCTURE_POOLS; }, 1));

    // Structure Processor Types
    public static final StructureProcessorType<RuinsStructureProcessor> RUINS_STRUCTURE_PROCESSOR_TYPE = StructureProcessorType
            .register(MODID + ":ruins-structure-processor", RuinsStructureProcessor.CODEC);
    public static final StructureProcessorType<OresStructureProcessor> ORES_STRUCTURE_PROCESSOR_TYPE = StructureProcessorType
            .register(MODID + ":ores-structure-processor", OresStructureProcessor.CODEC);

    // Structure Processor Lists
    public static final StructureProcessorList ENDER_OBELISK_PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":ender-obelisk-processor-list", new StructureProcessorList(Arrays.asList(
                    new RuinsStructureProcessor(0.05F,0.1F, 0.4F, 0.2F,0.3F, Arrays.asList(
                            Blocks.SPAWNER.getDefaultState(), Blocks.END_STONE.getDefaultState(),
                            Blocks.OBSIDIAN.getDefaultState(), Blocks.LAVA.getDefaultState(),
                            Blocks.END_STONE_BRICK_STAIRS.getDefaultState(), Blocks.END_STONE_BRICK_WALL.getDefaultState(),
                            Blocks.WATER.getDefaultState(), Blocks.CARVED_PUMPKIN.getDefaultState(),
                            Blocks.JACK_O_LANTERN.getDefaultState(), Blocks.CHEST.getDefaultState()
                    ))
            ))
    );
    public static final StructureProcessorList SILVERFISH_NEST_PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":silverfish-nest-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new OresStructureProcessor(true, 0.2f, 0, 0, 0, 0,
                            0, 0, 1, 0, 0),
                            new RuinsStructureProcessor(0.15F, 0, 0, 0, 0.75F, Arrays.asList(
                                    Blocks.SPAWNER.getDefaultState()
                            ))
                    ))
    );
    public static final StructureProcessorList WITHER_SKELETON_SHIP_PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":wither-skeleton-ship-processor-list", new StructureProcessorList(Arrays.asList(
                    new RuinsStructureProcessor(0.5F,0.10F, 0.0f, 0.3F,0.0F, Arrays.asList(
                            Blocks.SPAWNER.getDefaultState(), Blocks.LAVA.getDefaultState(),
                            Blocks.CHEST.getDefaultState(), Blocks.WITHER_SKELETON_SKULL.getDefaultState(),
                            Blocks.BONE_BLOCK.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(),
                            Blocks.NETHER_BRICK_WALL.getDefaultState()
                    ))
            ))
    );

    static {
        registerStructure(new Identifier(MODID + ":ender-obelisk"), ENDER_OBELISK_FEATURE,
                GenerationStep.Feature.LOCAL_MODIFICATIONS, 26, 24, 120301413,
                ENDER_OBELISK_FEATURE_CONFIGURED, false);
        registerStructure(new Identifier(MODID + ":silverfish-nest"), SILVERFISH_NEST_FEATURE,
                GenerationStep.Feature.UNDERGROUND_DECORATION, 32, 16, 502359193,
                SILVERFISH_NEST_FEATURE_CONFIGURED, false);
        registerStructure(new Identifier(MODID + ":wither-skeleton-ship"), WITHER_SKELETON_SHIP_FEATURE,
                GenerationStep.Feature.LOCAL_MODIFICATIONS, 48, 32, 858204813,
                WITHER_SKELETON_SHIP_FEATURE_CONFIGURED, false);

        //Registry.register(Registry.STRUCTURE_PIECE, new Identifier(MODID, "test_structure_piece"), PIECE);
    }

    //public static final StructurePieceType EXAMPLE_PIECE = StructurePieceType.register(
    //        EnderObeliskFeature.EnderCrystalPieces::new, MODID + ":ender-obelisk-crystal");


    @Override
    public void onInitializeServer() {
        // Register Structures
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(MODID + ":ender-obelisk"), ENDER_OBELISK_FEATURE_CONFIGURED);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(MODID + ":silverfish-nest"), SILVERFISH_NEST_FEATURE_CONFIGURED);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(MODID + ":wither-skeleton-ship"), WITHER_SKELETON_SHIP_FEATURE_CONFIGURED);


        // Register Features

        // Structures and features to biomes
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(MODID + ":ender-obelisk")));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(MODID + ":silverfish-nest")));
        BiomeModifications.addStructure(BiomeSelectors.foundInTheNether(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(MODID + ":wither-skeleton-ship")));
    }
}


