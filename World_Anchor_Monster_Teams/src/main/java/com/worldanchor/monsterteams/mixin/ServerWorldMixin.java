package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "spawnEntity", at = @At("HEAD"))
    void injectMethod(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity && entity.getScoreboardTeam() != null) {
            TeamUtil.addLivingEntityTeamMechanics((LivingEntity) entity);
        }
    }

}
