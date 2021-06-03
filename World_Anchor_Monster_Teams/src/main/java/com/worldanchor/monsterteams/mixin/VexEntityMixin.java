package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VexEntity.class)
public abstract class VexEntityMixin {

    @Inject(method = "setOwner", at = @At("HEAD"))
    void injectMethod(MobEntity owner, CallbackInfo ci) {
        if (owner.getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((VexEntity) (Object) this, owner.getScoreboardTeam().getName());
        }
    }

}
