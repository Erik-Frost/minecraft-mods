package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntityMixin{


    @Shadow protected abstract boolean hasExplosionEffects();

    @Shadow public abstract boolean wasShotAtAngle();

    @Inject(method = "explode", at = @At(value = "HEAD"))
    void injectMethod(CallbackInfo ci) {
        if (hasExplosionEffects() && (wasShotAtAngle() && getOwner() != null) && getOwner().getScoreboardTeam() != null) {
            // Get allies in range to fire on position and make them fire on the position
            List<MobEntity> mobs = ((FireworkRocketEntity)(Object)this).world.getEntitiesByClass(MobEntity.class, new Box(((FireworkRocketEntity)(Object)this).getBlockPos()),
                    EntityPredicates.VALID_ENTITY);

        }
    }



}
