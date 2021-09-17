package com.worldanchor.cripplingdebt;

import com.mojang.brigadier.Command;
import com.worldanchor.cripplingdebt.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.Random;

public class Mod implements ModInitializer {
    public static final String MODID = "world-anchor-crippling-debt";
    public static Config CONFIG;

    //GameRules
    public static final GameRules.Key<GameRules.IntegerValue> DAY_DURATION_DIVIDER =
            GameRuleRegistry.register("dayDurationDivider", GameRules.Category.MISC,
                    GameRuleFactory.createIntRule(2, 1));
    public static final GameRules.Key<GameRules.IntegerValue> NIGHT_DURATION_DIVIDER =
            GameRuleRegistry.register("nightDurationDivider", GameRules.Category.MISC,
                    GameRuleFactory.createIntRule(3, 1));

    @Override
    public void onInitialize() {
        // Read JSON Config
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.json"));
        CONFIG = gson.fromJson(reader, Config.class);
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            minecraftServer.getGameRules().getRule(DAY_DURATION_DIVIDER).set(CONFIG.dayDurationDivider, minecraftServer);
            minecraftServer.getGameRules().getRule(NIGHT_DURATION_DIVIDER).set(CONFIG.nightDurationDivider, minecraftServer);
            // Display hearts on the TAB scoreboard
            minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "scoreboard objectives add health health \"Health\"");
            minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "scoreboard objectives setdisplay list health");
            // Display money
            minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "scoreboard objectives add money dummy \"Money\"");
            minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "scoreboard objectives setdisplay sidebar money");
            // Make it so nights can never be slept through
            minecraftServer.getGameRules().getRule(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE).set(200, minecraftServer);
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(Commands.literal("revive")
                    .requires(source -> source.hasPermission(0))
                    .executes(context -> {
                            revive(context.getSource().getPlayerOrException());
                            return Command.SINGLE_SUCCESS;
                    }).then(Commands.argument("targets", EntityArgument.players())
                            .executes((context) -> {
                                for (ServerPlayer player : EntityArgument.getPlayers(context, "targets")) {
                                    revive(player);
                                }
                                return Command.SINGLE_SUCCESS;
                    }))
            );
            dispatcher.register(Commands.literal("sell")
                    .requires(source -> source.hasPermission(0))
                    .executes(context -> {
                        ItemStack itemStack = context.getSource().getPlayerOrException().getMainHandItem();
                        context.getSource().getServer().getCommands().performCommand(
                                context.getSource().getServer().createCommandSourceStack(),
                                "scoreboard players add " + context.getSource().getPlayerOrException().getName().getString()
                                        + " money " + itemStack.getCount());
                        itemStack.setCount(0);
                        return Command.SINGLE_SUCCESS;
                    })
            );
        });
    }

    public void revive(ServerPlayer player) {
        if (player.isSpectator()) {
            player.respawn();
            player.setGameMode(GameType.SURVIVAL);
        }
    }


    @Nullable
    public static Entity SpawnEnemy(EntityType<?> entityType, ServerLevel serverLevel, BlockPos center,
            Vec3 spawnRange, MobSpawnType mobSpawnType, @Nullable PlayerTeam team, int attempts,
            int distanceFromPlayers, String name, double enemyHealthMultiplier, double enemyDamageMultiplier,
            boolean canFly) {
        Entity enemy = null;
        // Attempt to find viable enemy spawn point while caring about collision
        BlockPos blockPos = FindViableEntitySpawnPointInBox(entityType, serverLevel, center, spawnRange, distanceFromPlayers,
                attempts, true, canFly);
        if (blockPos != null) {
            enemy = SpawnEntity(entityType, serverLevel, mobSpawnType, blockPos, team, name, true);
        }
        else {
            // Find viable spawn position without caring about collision
            blockPos = FindViableEntitySpawnPointInBox(entityType, serverLevel, center, spawnRange, distanceFromPlayers,
                    attempts, false, canFly);
            if (blockPos != null) {
                // Destroy a block and create explosion in world to make room for enemy
                serverLevel.destroyBlock(blockPos, false);
                serverLevel.explode(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        (float)(4.0D + serverLevel.random.nextDouble() * 4.0D), Explosion.BlockInteraction.BREAK);
                // Spawn enemy after last attempt regardless of environmental conditions
                enemy = SpawnEntity(entityType, serverLevel, mobSpawnType, blockPos, team, name, true);
                // Give enemy damage resistance to resist the explosion damage
                if (enemy instanceof LivingEntity livingEnemy) livingEnemy.addEffect(
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 3));
            }
        }
        if (enemy instanceof LivingEntity livingEnemy) {
            // Make the follow range of the entity 160 so they can track from much farther
            Objects.requireNonNull(livingEnemy.getAttributes()
                    .getInstance(Attributes.FOLLOW_RANGE)).setBaseValue(160d);
            // Also exponentially scale health and damage
            double enemyBaseHealth = Objects.requireNonNull(livingEnemy.getAttributes()
                    .getInstance(Attributes.MAX_HEALTH)).getValue();
            Objects.requireNonNull(livingEnemy.getAttributes()
                    .getInstance(Attributes.MAX_HEALTH)).setBaseValue(enemyBaseHealth * enemyHealthMultiplier);
            double enemyBaseDamage = Objects.requireNonNull(livingEnemy.getAttributes()
                    .getInstance(Attributes.ATTACK_DAMAGE)).getValue();
            Objects.requireNonNull(livingEnemy.getAttributes()
                    .getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(enemyBaseDamage * enemyDamageMultiplier);
            // Make sure new enemy is fully healed
            livingEnemy.setHealth(livingEnemy.getMaxHealth());
        }
        return enemy;
    }

    public static BlockPos FindViableEntitySpawnPointInBox(EntityType<?> entityType, ServerLevel serverLevel,
            BlockPos boxCenter, Vec3 boxSize, int distanceFromPlayers, int attempts, boolean checkCollision,
            boolean canEntityFly) {
        // Attempt to find a place to spawn entity in
        for (int i = 0; i <= attempts; i += 1) {
            // Choose random point in box
            Vec3 pos = getRandomPointInBox(boxSize, new Vec3(boxCenter.getX(), boxCenter.getY(), boxCenter.getZ()));
            BlockPos blockPos = new BlockPos(pos);
            // Make sure random point above the min build height
            if (serverLevel.getMinBuildHeight() > blockPos.getY()) continue;
            // Check if entity can spawn at pos without colliding into anything
            if (checkCollision && !serverLevel.noCollision(entityType.getAABB(blockPos.getX(),
                    blockPos.getY(), blockPos.getZ()))) continue;
            // If entity can't fly, place it as close as possible to the ground
            if (!canEntityFly) {
                while (serverLevel.noCollision(entityType.getAABB(blockPos.getX(),
                        blockPos.getY() - 1, blockPos.getZ()))
                        && (serverLevel.getBlockState(blockPos).is(Blocks.AIR)
                        || serverLevel.getBlockState(blockPos).is(Blocks.CAVE_AIR))) {
                    blockPos = blockPos.below(1);
                    pos = pos.add(0, -1, 0);
                }
            }
            // Make sure random point far enough way from players
            if (!pointIsFarEnoughAwayFromPlayers(pos,serverLevel,distanceFromPlayers)) continue;
            // Avoid spawning in or over a dangerous block
            // Most often this is lava or fire but can also be rarer things like cactus
            // This can also be water in the case of a blaze
            if (DangerousBlocksBelowEntitySpawnPoint(entityType, serverLevel, blockPos)) continue;
            // Viable position found
            return blockPos;
        }
        return null;
    }

    public static boolean DangerousBlocksBelowEntitySpawnPoint(EntityType<?> entityType,
            ServerLevel serverLevel, BlockPos blockPos) {
        // Avoid spawning in or over a dangerous block
        // Most often this is lava or fire but can also be rarer things like cactus
        // This can also be water in the case of a blaze
        for (int x = -Math.round(entityType.getWidth()/2);
                x <= Math.round(entityType.getWidth()/2); x += 1) {
            for (int z = -Math.round(entityType.getWidth()/2);
                    z <= Math.round(entityType.getWidth()/2); z += 1) {
                if (entityType.isBlockDangerous(serverLevel.getBlockState(blockPos.offset(x, 0, z)))
                        || entityType.isBlockDangerous(serverLevel.getBlockState(blockPos.offset(x,-1,z))))
                    return true;
            }
        }
        return false;
    }

    public static Entity SpawnEntity(EntityType<?> entityType, ServerLevel serverLevel, MobSpawnType mobSpawnType,
            BlockPos blockPos, @Nullable PlayerTeam playerTeam, String name, boolean setPersistant) {
        Entity newEntity = entityType.create(serverLevel);
        if (newEntity != null) {
            // Make entity persistant
            if (setPersistant && newEntity instanceof Mob) ((Mob)newEntity).setPersistenceRequired();
            // Set entity's custom name
            if (name != null) newEntity.setCustomName(new TextComponent(name));
            // Add entity(s) to team!
            if (playerTeam != null) {
                serverLevel.getScoreboard().addPlayerToTeam(newEntity.getStringUUID(), playerTeam);
                for (Entity entity: newEntity.getPassengers()) {
                    serverLevel.getScoreboard().addPlayerToTeam(entity.getStringUUID(),playerTeam);
                }
            }
            // Set entity(s)'s position
            newEntity.absMoveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                    serverLevel.random.nextFloat() * 360.0F, 0.0F);
            // Initialize
            if (newEntity instanceof Mob) ((Mob)newEntity).finalizeSpawn(serverLevel,
                    serverLevel.getCurrentDifficultyAt(blockPos), mobSpawnType, null, null);
            // Spawn entity(s)
            serverLevel.addFreshEntityWithPassengers(newEntity);
            // Make particle effect spawn particle effect
            serverLevel.levelEvent(2004, blockPos, 0);
            if (newEntity instanceof Mob) {
                ((Mob)newEntity).spawnAnim();
            }
            return newEntity;
        }
        else return null;
    }

    public static Vec3 getRandomPointInBox(Vec3 boxSize, Vec3 offset) {
        Random random = new Random();
        double x = offset.x + (random.nextDouble() - random.nextDouble()) * (boxSize.x/2);
        double y = offset.y + (random.nextDouble() - random.nextDouble()) * (boxSize.y/2);
        double z = offset.z + (random.nextDouble() - random.nextDouble()) * (boxSize.z/2);
        return new Vec3(x, y, z);
    }

    public static boolean pointIsFarEnoughAwayFromPlayers(Vec3 point, ServerLevel serverLevel, double distance) {
        for (ServerPlayer player : serverLevel.players()) {
            if (player.isAlive() && !player.isSpectator()
                    && player.distanceToSqr(point) < (distance * distance))
                return false;
        }
        return true;
    }
}
