package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin {

    @Inject(method = "setOwner", at = @At("HEAD"))
    void injectMethod(PlayerEntity player, CallbackInfo ci) {
        if (player.getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((TameableEntity) (Object) this, player.getScoreboardTeam().getName());
        }
    }

}
