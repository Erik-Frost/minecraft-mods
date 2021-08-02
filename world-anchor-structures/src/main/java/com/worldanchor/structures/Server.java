package com.worldanchor.structures;

import com.worldanchor.structures.features.*;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

@SuppressWarnings("deprecation")
public class Server implements DedicatedServerModInitializer {
    public static final String MODID = "world-anchor-structures";

    @Override
    public void onInitializeServer() {
        // Add Structures and features to biomes
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(BiomeKeys.WARPED_FOREST),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, AlchemistLabFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, EnchantersSmithyFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.Category.OCEAN, Biome.Category.BEACH, Biome.Category.RIVER).negate()),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, EnderObeliskFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(BiomeKeys.NETHER_WASTES),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, GhastTowerFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.JUNGLE, Biome.Category.SAVANNA, Biome.Category.TAIGA))
                        .and(BiomeSelectors.categories(Biome.Category.BEACH, Biome.Category.RIVER).negate()),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, GiantBeehiveFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.JUNGLE, Biome.Category.SAVANNA, Biome.Category.TAIGA))
                        .and(BiomeSelectors.categories(Biome.Category.BEACH, Biome.Category.RIVER).negate()),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, HuntersRespiteFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, SilverfishNestFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.Category.OCEAN, Biome.Category.BEACH, Biome.Category.RIVER).negate()),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, SmugglersCacheFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.SWAMP),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, WitchHouseFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInTheNether(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, WitherSkeletonShipFeature.ID));

    }
}


