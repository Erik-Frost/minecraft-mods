package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TrackTargetGoal.class)
public abstract class TrackTargetGoalMixin {

    @Shadow @Final protected MobEntity mob;

}
