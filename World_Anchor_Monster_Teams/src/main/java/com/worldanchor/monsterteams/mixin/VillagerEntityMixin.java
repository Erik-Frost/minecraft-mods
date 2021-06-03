package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {

    @Redirect(method = "spawnIronGolem", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"))
    void redirectIronGolemSpawn(ServerWorld serverWorld, Entity entity) {
        if (((VillagerEntity) (Object) this).getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((VillagerEntity) (Object) this).getScoreboardTeam().getName());
        }
        serverWorld.spawnEntityAndPassengers(entity);
    }

    @Redirect(method = "onStruckByLightning", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"))
    void redirectWitchSpawn(ServerWorld serverWorld, Entity entity) {
        if (((VillagerEntity) (Object) this).getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((VillagerEntity) (Object) this).getScoreboardTeam().getName());
        }
        serverWorld.spawnEntityAndPassengers(entity);
    }

}
