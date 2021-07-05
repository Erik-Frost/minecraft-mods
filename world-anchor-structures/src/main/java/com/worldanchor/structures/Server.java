package com.worldanchor.structures;

import com.google.common.collect.ImmutableList;
import com.worldanchor.structures.processors.*;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;

public class Server implements DedicatedServerModInitializer {
    public static final String MODID = "world-anchor-structures";

    // Identifiers
    public static final Identifier BASE_POOL = new Identifier(MODID, ":base_pool");
    public static final Identifier ENDER_OBELISK_ID = new Identifier(MODID, ":ender-obelisk");


    public static final StructureFeature<StructurePoolFeatureConfig> ENDER_OBELISK_FEATURE =
            new EnderObeliskFeature(StructurePoolFeatureConfig.CODEC);

    public static final ConfiguredStructureFeature<StructurePoolFeatureConfig,
            ? extends StructureFeature<StructurePoolFeatureConfig>> ENDER_OBELISK_FEATURE_CONFIGURED
            = ENDER_OBELISK_FEATURE.configure(new StructurePoolFeatureConfig(() -> {
                return EnderObeliskFeature.EnderCrystalPieces.STRUCTURE_POOLS; }, 1));



    // Structure Processor Types
    public static final StructureProcessorType<RuinsStructureProcessor> RUINS_STRUCTURE_PROCESSOR_TYPE = StructureProcessorType
            .register(MODID + ":ruins-structure-processor", RuinsStructureProcessor.CODEC);

    // Structure Processor Lists
    public static final StructureProcessorList ENDER_OBELISK_PROCESSOR_LIST = BuiltinRegistries.add(
            BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, MODID + ":ender-obelisk-processor-list", new StructureProcessorList(ImmutableList.of(
                    new RuinsStructureProcessor(0.05F,0.1F, 0.4F, 0.2F,0.3F, ImmutableList.of(
                            Blocks.SPAWNER.getDefaultState(), Blocks.END_STONE.getDefaultState(),
                            Blocks.OBSIDIAN.getDefaultState(), Blocks.LAVA.getDefaultState(),
                            Blocks.END_STONE_BRICK_STAIRS.getDefaultState(), Blocks.END_STONE_BRICK_WALL.getDefaultState(),
                            Blocks.WATER.getDefaultState(), Blocks.CARVED_PUMPKIN.getDefaultState(),
                            Blocks.JACK_O_LANTERN.getDefaultState(), Blocks.CHEST.getDefaultState()
                    ))
            ))
    );

    static {
        registerStructure(ENDER_OBELISK_ID, ENDER_OBELISK_FEATURE,
                GenerationStep.Feature.LOCAL_MODIFICATIONS, 26, 24, 120301413,
                ENDER_OBELISK_FEATURE_CONFIGURED, false);

        //Registry.register(Registry.STRUCTURE_PIECE, new Identifier(MODID, "test_structure_piece"), PIECE);
    }

    public static final StructurePieceType EXAMPLE_PIECE = StructurePieceType.register(
            EnderObeliskFeature.EnderCrystalPieces::new, MODID + ":ender-obelisk-crystal");


    @Override
    public void onInitializeServer() {
        // Register Structures
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ENDER_OBELISK_ID, ENDER_OBELISK_FEATURE_CONFIGURED);
        // Register Features

        // Structures and features to biomes
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, ENDER_OBELISK_ID));
    }

    public static <FC extends FeatureConfig, S extends StructureFeature<FC>> void registerStructure(Identifier id, S f, GenerationStep.Feature step, int spacing, int separation, int salt,  ConfiguredStructureFeature<FC, ? extends StructureFeature<FC>> c, boolean adjustsSurface) {
        FabricStructureBuilder<FC, S> builder = FabricStructureBuilder.create(id, f)
                .step(step)
                .defaultConfig(spacing, separation, salt)
                .superflatFeature(c);
        if (adjustsSurface) {
            builder.adjustsSurface();
        }
        builder.register();
    }

}


