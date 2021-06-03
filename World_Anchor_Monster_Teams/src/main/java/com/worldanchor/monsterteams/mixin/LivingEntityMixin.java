package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin{

    @Inject(method = "shouldDropLoot", at = @At("RETURN"), cancellable = true)
    private void injectShouldDropLoot(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && ((LivingEntity) (Object) this).getScoreboardTeam() == null);
    }
}
