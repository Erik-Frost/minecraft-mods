package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Vex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public abstract class VexEntityMixin {

    @Inject(method = "setOwner", at = @At("HEAD"))
    void injectMethod(Mob owner, CallbackInfo ci) {
        if (owner.getTeam() != null) {
            TeamUtil.addToTeamHelper((Vex) (Object) this, owner.getTeam().getName());
        }
    }

}
