package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {



    @Redirect(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;isAcceptableSpawnPosition(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z"))
    private static boolean injectMethod(ServerWorld world, Chunk chunk, BlockPos.Mutable pos, double squaredDistance) {
        for (LivingEntity livingEntity : world.getEntitiesIncludingUngeneratedChunks(LivingEntity.class, new Box(pos).expand(18), null)) {
            if (livingEntity.getScoreboardTeam() != null && 16 > livingEntity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ())) return false;
        }
        return SpawnHelperAccessor.invokeIsAcceptableSpawnPosition(world, chunk, pos, squaredDistance);

    }
    
}
