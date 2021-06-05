package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin {

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "addPlayerToTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;"))
    void injectAddPlayerToTeam(String name, Team team, CallbackInfoReturnable<Boolean> cir) {
        if (name.length() > 16) {
            // Is non player entity
            Entity entity = null;
            for (ServerWorld world : server.getWorlds()) {
                // For each world
                entity = world.getEntity(UUID.fromString(name));
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    // Add living entity team mechanics
                    TeamUtil.addLivingEntityTeamMechanics(livingEntity);
                    break;
                }
            }

        }
    }

    @Inject(method = "removePlayerFromTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;"))
    void injectRemovePlayerFromTeam(String name, Team team, CallbackInfo ci) {
        if (name.length() > 16) {
            // Is non player entity
            Entity entity = null;
            for (ServerWorld world : server.getWorlds()) {
                // For each world
                entity = world.getEntity(UUID.fromString(name));
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    // Turn off name tag always display
                    livingEntity.setCustomNameVisible(false);
                    // Remove team goals from mobEntity
                    if (livingEntity instanceof MobEntity) TeamUtil.removeTeamGoals((MobEntity) livingEntity);
                    break;
                }
            }

        }
    }
}
