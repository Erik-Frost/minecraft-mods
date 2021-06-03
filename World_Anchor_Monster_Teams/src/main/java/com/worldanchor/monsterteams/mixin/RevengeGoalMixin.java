package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RevengeGoal.class)
public abstract class RevengeGoalMixin extends TrackTargetGoalMixin{


    @Shadow protected abstract void setMobEntityTarget(MobEntity mob, LivingEntity target);

    @Redirect(method = "callSameTypeForRevenge", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/RevengeGoal;setMobEntityTarget(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/LivingEntity;)V"))
    void redirectSetMobEntityTarget(RevengeGoal revengeGoal, MobEntity ally, LivingEntity target) {
        if (ally.getScoreboardTeam() == mob.getScoreboardTeam()) {
            setMobEntityTarget(ally, target);
        }
    }
}
