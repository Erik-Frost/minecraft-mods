package com.worldanchor.structures.mixin;

import com.worldanchor.structures.features.EnderObeliskFeature;
import com.worldanchor.structures.features.GiantBeehiveFeature;
import com.worldanchor.structures.features.SilverfishNestFeature;
import com.worldanchor.structures.features.WitherSkeletonShipFeature;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseChunkGenerator.class)
public class NoiseChunkGeneratorMixin {

    @Inject(at = @At("HEAD"), method = "getEntitySpawnList", cancellable = true)
    public void injectSpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos, CallbackInfoReturnable<Pool<SpawnSettings.SpawnEntry>> info) {
        if (accessor.getStructureAt(pos, true, EnderObeliskFeature.DEFAULT).hasChildren()) {
            if (group == SpawnGroup.MONSTER) info.setReturnValue(EnderObeliskFeature.MONSTER_SPAWNS);
        }
        else if (accessor.getStructureAt(pos, true, SilverfishNestFeature.DEFAULT).hasChildren()) {
            if (group == SpawnGroup.MONSTER) info.setReturnValue(SilverfishNestFeature.MONSTER_SPAWNS);
        }
        else if (accessor.getStructureAt(pos, true, WitherSkeletonShipFeature.DEFAULT).hasChildren()) {
            if (group == SpawnGroup.MONSTER) info.setReturnValue(StructureFeature.FORTRESS.getMonsterSpawns());
        }
    }

}
