package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Pig.class)
public abstract class PigEntityMixin {

    @Redirect(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    boolean redirectMethod(ServerLevel serverLevel, Entity entity) {
        if (((Pig) (Object) this).getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((Pig) (Object) this).getTeam().getName());
        }
        return serverLevel.addFreshEntity(entity);
    }

}
