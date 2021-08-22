package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin {

    @Inject(method = "tame", at = @At("HEAD"))
    void injectMethod(Player player, CallbackInfo ci) {
        if (player.getTeam() != null) {
            TeamUtil.addToTeamHelper((TamableAnimal) (Object) this, player.getTeam().getName());
        }
    }

}
