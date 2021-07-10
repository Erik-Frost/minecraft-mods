package com.worldanchor.qol;

import com.mojang.brigadier.Command;
import com.worldanchor.qol.mixin.ServerWorldAccessor;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Random;

import static net.minecraft.server.command.CommandManager.literal;

public class Server implements DedicatedServerModInitializer {
    public static final String MODID = "world-anchor-qol";

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("spawn")
                    .requires(source -> source.hasPermissionLevel(0))
                            .executes(context -> {
                                ServerCommandSource source = context.getSource();
                                teleportPlayerWithMountAndPassengers(source.getWorld().getSpawnPos(), source.getPlayer(),
                                        source.getWorld(), source.getServer());
                                return Command.SINGLE_SUCCESS;
                            })
            );
            dispatcher.register(literal("home")
                    .requires(source -> source.hasPermissionLevel(0))
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        if (source.getPlayer().getSpawnPointPosition() != null) {
                            teleportPlayerWithMountAndPassengers(source.getPlayer().getSpawnPointPosition().up(1), source.getPlayer(),
                                    source.getWorld(), source.getServer());
                        }
                        return Command.SINGLE_SUCCESS;
                    })
            );
        });
    }

    private void teleportPlayerWithMountAndPassengers(BlockPos target, ServerPlayerEntity player, ServerWorld world, MinecraftServer server){
        Entity vehicle = player.getVehicle();
        List<Entity> passengers = player.getPassengerList();
        Random random = new Random();
        // Spawn particles at old location
        ParticleS2CPacket particleS2CPacket = new ParticleS2CPacket(ParticleTypes.PORTAL, true, player.getX(), player.getY() + random.nextDouble() * 2.0D, player.getZ(), (float)random.nextGaussian(), 0.0f, (float)random.nextGaussian(), 1, 100);
        for(int i = 0; i < world.getPlayers().size(); ++i) {
            ((ServerWorldAccessor)world).sendToPlayerIfNearby(world.getPlayers().get(i), true, player.getX(), player.getY(), player.getZ(), particleS2CPacket);
        }
        world.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + random.nextDouble() * 2.0D, player.getZ(), 100, random.nextGaussian(), 0.0D, random.nextGaussian(), 1);
        // Teleport player to world spawn
        player.teleport(server.getOverworld(),
                target.getX(), target.getY(), target.getZ(),
                player.headYaw, player.getPitch());
        // Teleport vehicle to world spawn with player
        if (vehicle != null) {
            vehicle.moveToWorld(server.getOverworld());
            vehicle.teleport(target.getX(), target.getY(), target.getZ());
        }
        // Teleport passengers to world spawn with player
        for (Entity passenger : passengers) {
            passenger.moveToWorld(server.getOverworld());
            passenger.teleport(target.getX(), target.getY(), target.getZ());
        }
        // Spawn particles at new location
        ParticleS2CPacket particleS2CPacket2 = new ParticleS2CPacket(ParticleTypes.PORTAL, true, target.getX(), target.getY() + random.nextDouble() * 2.0D, target.getZ(), (float)random.nextGaussian(), 0.0f, (float)random.nextGaussian(), 1, 100);
        for(int i = 0; i < world.getPlayers().size(); ++i) {
            ((ServerWorldAccessor)world).sendToPlayerIfNearby(world.getPlayers().get(i), true, target.getX(), target.getY(), target.getZ(), particleS2CPacket2);
        }
    }

}


