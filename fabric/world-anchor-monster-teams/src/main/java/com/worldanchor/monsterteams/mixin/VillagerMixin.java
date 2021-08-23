package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Villager.class)
public abstract class VillagerMixin {

    @Redirect(method = "trySpawnGolem", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    void redirectIronGolemSpawn(ServerLevel ServerLevel, Entity entity) {
        if (((Villager) (Object) this).getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((Villager) (Object) this).getTeam().getName());
        }
        ServerLevel.addFreshEntityWithPassengers(entity);
    }

    @Redirect(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"))
    void redirectWitchSpawn(ServerLevel ServerLevel, Entity entity) {
        if (((Villager) (Object) this).getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((Villager) (Object) this).getTeam().getName());
        }
        ServerLevel.addFreshEntityWithPassengers(entity);
    }

}
