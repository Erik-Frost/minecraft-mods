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
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(BiomeKeys.CRIMSON_FOREST, BiomeKeys.WARPED_FOREST),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, AlchemistLabFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, EnchantersSmithyFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, EnderObeliskFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.JUNGLE, Biome.Category.SAVANNA, Biome.Category.TAIGA)),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, GiantBeehiveFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.FOREST)),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, HuntersRespiteFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, SilverfishNestFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.FOREST)),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, SmugglersCacheFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInTheNether(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, WitherSkeletonShipFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInTheNether(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, GhastTowerFeature.ID));
    }
}


