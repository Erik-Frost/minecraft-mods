package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseBaseEntity.class)
public abstract class HorseBaseEntityMixin {

    @Inject(method = "bondWithPlayer", at = @At("HEAD"))
    void injectMethod(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player.getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((HorseBaseEntity) (Object) this, player.getScoreboardTeam().getName());
        }
    }

}
