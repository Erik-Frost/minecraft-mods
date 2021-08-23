package com.worldanchor.structures.mixin;

import com.worldanchor.structures.features.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {

    @Inject(at = @At("HEAD"), method = "getMobsAt", cancellable = true)
    public void injectSpawnList(Biome biome, StructureFeatureManager structureFeatureManager, MobCategory mobCategory,
            BlockPos blockPos, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir) {
        if (structureFeatureManager.getStructureAt(blockPos, true, EnderObeliskFeature.DEFAULT).isValid()) {
            if (mobCategory == MobCategory.MONSTER) cir.setReturnValue(EnderObeliskFeature.MONSTER_SPAWNS);
        }
        else if (structureFeatureManager.getStructureAt(blockPos, true, SilverfishNestFeature.DEFAULT).isValid()) {
            if (mobCategory == MobCategory.MONSTER) cir.setReturnValue(SilverfishNestFeature.MONSTER_SPAWNS);
        }
        else if (structureFeatureManager.getStructureAt(blockPos, true, WitchHouseFeature.DEFAULT).isValid()) {
            if (mobCategory == MobCategory.MONSTER) cir.setReturnValue(WitchHouseFeature.MONSTER_SPAWNS);
        }
        else if (structureFeatureManager.getStructureAt(blockPos, true, WitherSkeletonShipFeature.DEFAULT).isValid()) {
            if (mobCategory == MobCategory.MONSTER) cir.setReturnValue(StructureFeature.NETHER_BRIDGE.getSpecialEnemies());
        }
    }

}
