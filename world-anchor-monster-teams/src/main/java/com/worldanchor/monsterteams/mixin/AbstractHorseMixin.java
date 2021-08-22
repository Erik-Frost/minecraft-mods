package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {

    @Inject(method = "tameWithName", at = @At("HEAD"))
    void injectMethod(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player.getTeam() != null) {
            TeamUtil.addToTeamHelper((AbstractHorse) (Object) this, player.getTeam().getName());
        }
    }

}
