package com.worldanchor.monsterteams.mixin;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HurtByTargetGoal.class)
public abstract class HurtByTargetGoalMixin extends TrackTargetGoalMixin{


    @Shadow protected abstract void alertOther(Mob mob, LivingEntity target);

    @Redirect(method = "alertOthers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/target/HurtByTargetGoal;alertOther(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/entity/LivingEntity;)V"))
    void redirectSetMobEntityTarget(HurtByTargetGoal hurtByTargetGoal, Mob ally, LivingEntity target) {
        if (ally.getTeam() == mob.getTeam()) {
            alertOther(ally, target);
        }
    }
}
