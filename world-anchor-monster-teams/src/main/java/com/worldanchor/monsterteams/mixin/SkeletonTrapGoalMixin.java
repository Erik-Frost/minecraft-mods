package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.SkeletonTrapGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonTrapGoal.class)
public abstract class SkeletonTrapGoalMixin {

    @Shadow @Final private SkeletonHorse horse;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    void redirectSkeleton(ServerLevel ServerLevel, Entity entity) {
        if (horse.getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, horse.getTeam().getName());
            for (Entity passenger: entity.getIndirectPassengers()) {
                TeamUtil.addToTeamHelper((LivingEntity) passenger, horse.getTeam().getName());
            }
        }
        ServerLevel.addFreshEntityWithPassengers(entity);
    }

}
