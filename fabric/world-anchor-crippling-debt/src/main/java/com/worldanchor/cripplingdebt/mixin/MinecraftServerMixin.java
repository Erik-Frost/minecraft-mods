package com.worldanchor.cripplingdebt.mixin;

import com.worldanchor.cripplingdebt.Mod;
import com.worldanchor.cripplingdebt.config.Enemy;
import com.worldanchor.cripplingdebt.config.SpawnRate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.util.Mth.floor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract ServerLevel overworld();

    @Shadow private PlayerList playerList;

    @Shadow @Final private Random random;

    @Redirect(method = "setDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;isHardcore()Z"))
    private boolean redirectIsHardcore(WorldData worldData) {
        return false;
    }

    @Inject(method = "tickChildren", at = @At(value = "HEAD"))
    private void serverTick(CallbackInfo info) {
        //night is from 13000 to 23000
        if (13000 <= overworld().getLevelData().getDayTime() % 24000 &&
                overworld().getLevelData().getDayTime() % 24000 < 23000) {
            double currentDay = (overworld().getGameTime() / Mod.CONFIG.getDayCycleDuration()) + 1;
            double enemyHealthMultiplier = Math.pow(Mod.CONFIG.multiplyEnemyHealthEachDayBy, currentDay);
            double enemyDamageMultiplier = Math.pow(Mod.CONFIG.multiplyEnemyDamageEachDayBy, currentDay);
            ArrayList<Player> alivePlayers = new ArrayList<>();
            for (ServerPlayer player : playerList.getPlayers()) {
                if (player.isAlive() && player.gameMode.isSurvival()) alivePlayers.add(player);
            }
            // Spawn enemies
            if (alivePlayers.size() > 0) {
                for (Enemy enemy : Mod.CONFIG.enemies) {
                    // Find the latest spawnrate
                    int latest = 0;
                    double spawnChance = 0;
                    for (SpawnRate spawnRate : enemy.spawnRates) {
                        if (spawnRate.startingAtNight >= latest && spawnRate.startingAtNight <= (int) currentDay) {
                            spawnChance = (spawnRate.averageSpawnsEachNight +
                                    (spawnRate.averageSpawnsEachNight * (alivePlayers.size() - 1)
                                            * (Mod.CONFIG.percentIncreaseOfEnemySpawnsForEachExtraAlivePlayer/100))) /
                                    Mod.CONFIG.getNightDuration();
                            latest = spawnRate.startingAtNight;
                        }
                    }
                    // Check if enemy should be spawned
                    if (spawnChance >= random.nextFloat()) {
                        // Spawn near random alive player
                        Player player = alivePlayers.get(random.nextInt(alivePlayers.size()));
                        Mod.SpawnEnemy(EntityType.byString(enemy.entity).get(), (ServerLevel) player.level,
                                player.getOnPos(), new Vec3(160, 160, 160), MobSpawnType.COMMAND,
                                null, 5000, 24, enemy.name, enemyHealthMultiplier,
                                enemyDamageMultiplier, enemy.canFly);
                    }

                }
            }



        }

    }
}
