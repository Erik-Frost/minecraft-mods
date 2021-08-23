package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Zombie.class)
public abstract class ZombieMixin {

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    void redirectMethod(ServerLevel ServerLevel, Entity entity) {
        if (((Zombie) (Object) this).getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((Zombie) (Object) this).getTeam().getName());
        }
        ServerLevel.addFreshEntityWithPassengers(entity);
    }

}
