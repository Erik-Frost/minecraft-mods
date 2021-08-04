package com.worldanchor.structures;

import com.worldanchor.structures.features.*;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("deprecation")
public class Server implements DedicatedServerModInitializer {
    public static final String MODID = "world-anchor-structures";

    @Override
    public void onInitializeServer() {

        // Add Structures and features to biomes
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(Biomes.WARPED_FOREST),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, AlchemistLabFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, EnchantersSmithyFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.BiomeCategory.OCEAN, Biome.BiomeCategory.BEACH, Biome.BiomeCategory.RIVER).negate()),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, EnderObeliskFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(Biomes.NETHER_WASTES),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, GhastTowerFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.JUNGLE, Biome.BiomeCategory.SAVANNA, Biome.BiomeCategory.TAIGA))
                        .and(BiomeSelectors.categories(Biome.BiomeCategory.BEACH, Biome.BiomeCategory.RIVER).negate()),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, GiantBeehiveFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.JUNGLE, Biome.BiomeCategory.SAVANNA, Biome.BiomeCategory.TAIGA))
                        .and(BiomeSelectors.categories(Biome.BiomeCategory.BEACH, Biome.BiomeCategory.RIVER).negate()),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, HuntersRespiteFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, SilverfishNestFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.categories(Biome.BiomeCategory.OCEAN, Biome.BiomeCategory.BEACH, Biome.BiomeCategory.RIVER).negate()),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, SmugglersCacheFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.BiomeCategory.SWAMP),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, WitchHouseFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInTheNether(),
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, WitherSkeletonShipFeature.ID));

    }
}


