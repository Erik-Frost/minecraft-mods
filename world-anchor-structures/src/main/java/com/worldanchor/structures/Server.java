package com.worldanchor.structures;

import com.worldanchor.structures.features.EnderObeliskFeature;
import com.worldanchor.structures.features.GiantBeehiveFeature;
import com.worldanchor.structures.features.SilverfishNestFeature;
import com.worldanchor.structures.features.WitherSkeletonShipFeature;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

@SuppressWarnings("deprecation")
public class Server implements DedicatedServerModInitializer {
    public static final String MODID = "world-anchor-structures";

    @Override
    public void onInitializeServer() {
        // Add Structures and features to biomes
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, EnderObeliskFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.FOREST)),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, GiantBeehiveFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, SilverfishNestFeature.ID));
        BiomeModifications.addStructure(BiomeSelectors.foundInTheNether(),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, WitherSkeletonShipFeature.ID));
    }
}


