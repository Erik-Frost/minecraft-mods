package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
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

    @Inject(method = "addPlayerToTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerList()Lnet/minecraft/server/players/PlayerList;"))
    void injectAddPlayerToTeam(String name, PlayerTeam playerTeam, CallbackInfoReturnable<Boolean> cir) {
        if (name.length() > 16) {
            // Is non player entity
            Entity entity = null;
            for (ServerLevel world : server.getAllLevels()) {
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

    @Inject(method = "removePlayerFromTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerList()Lnet/minecraft/server/players/PlayerList;"))
    void injectRemovePlayerFromTeam(String name, PlayerTeam playerTeam, CallbackInfo ci) {
        if (name.length() > 16) {
            // Is non player entity
            Entity entity = null;
            for (ServerLevel world : server.getAllLevels()) {
                // For each world
                entity = world.getEntity(UUID.fromString(name));
                if (entity instanceof LivingEntity livingEntity) {
                    // Turn off name tag always display
                    livingEntity.setCustomNameVisible(false);
                    // Remove team goals from mobEntity
                    if (livingEntity instanceof Mob) TeamUtil.removeTeamGoals((Mob) livingEntity);
                    break;
                }
            }

        }
    }
}
