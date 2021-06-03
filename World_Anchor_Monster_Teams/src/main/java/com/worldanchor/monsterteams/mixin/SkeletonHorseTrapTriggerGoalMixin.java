package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonHorseTrapTriggerGoal.class)
public abstract class SkeletonHorseTrapTriggerGoalMixin {

    @Shadow @Final private SkeletonHorseEntity skeletonHorse;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"))
    void redirectSkeleton(ServerWorld serverWorld, Entity entity) {
        if (skeletonHorse.getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, skeletonHorse.getScoreboardTeam().getName());
            for (Entity passenger: entity.getPassengersDeep()) {
                TeamUtil.addToTeamHelper((LivingEntity) passenger, skeletonHorse.getScoreboardTeam().getName());
            }
        }
        serverWorld.spawnEntityAndPassengers(entity);
    }

}
