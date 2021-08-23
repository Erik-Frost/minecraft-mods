package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CombatTracker.class)
public abstract class CombatTrackerMixin {

    @Shadow @Final private LivingEntity mob;

    @Inject(method = "recordDamage", at = @At("HEAD"))
    void injectMethod(DamageSource source, float originalHealth, float f, CallbackInfo ci) {
        if (mob.getTeam() != null && source.getEntity() != null
            && mob.getTeam() != source.getEntity().getTeam()
            && source.getEntity() instanceof LivingEntity
            && !((source.getEntity() instanceof Player) && ((Player)source.getEntity()).isCreative())
            && !source.getEntity().isSpectator()) {
            TeamUtil.alertOthersOnTeam(mob, (LivingEntity) source.getEntity());
        }
    }
}
