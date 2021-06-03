package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin {

    @Redirect(method = "onStruckByLightning", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    boolean redirectMethod(ServerWorld serverWorld, Entity entity) {
        if (((PigEntity) (Object) this).getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((PigEntity) (Object) this).getScoreboardTeam().getName());
        }
        return serverWorld.spawnEntity(entity);
    }

}
