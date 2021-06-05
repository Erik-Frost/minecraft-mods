package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin{

    @Shadow @Final private LivingEntity entity;

    @Inject(method = "onDamage", at = @At("HEAD"))
    void injectMethod(DamageSource source, float originalHealth, float f, CallbackInfo ci) {
        if (entity.getScoreboardTeam() != null && source.getAttacker() != null
            && entity.getScoreboardTeam() != source.getAttacker().getScoreboardTeam()
            && source.getAttacker() instanceof LivingEntity
            && !((source.getAttacker() instanceof PlayerEntity) && ((PlayerEntity)source.getAttacker()).isCreative())
            && !source.getAttacker().isSpectator()) {
            TeamUtil.alertOthersOnTeam(entity, (LivingEntity) source.getAttacker());
        }
    }
}
