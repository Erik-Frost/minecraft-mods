package com.worldanchor.monsterteams.mixin;


import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TargetGoal.class)
public abstract class TrackTargetGoalMixin {

    @Shadow @Final protected Mob mob;

}
