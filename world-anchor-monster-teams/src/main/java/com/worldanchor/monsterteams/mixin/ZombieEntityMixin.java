package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"))
    void redirectMethod(ServerWorld serverWorld, Entity entity) {
        if (((ZombieEntity) (Object) this).getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((ZombieEntity) (Object) this).getScoreboardTeam().getName());
        }
        serverWorld.spawnEntityAndPassengers(entity);
    }

}
