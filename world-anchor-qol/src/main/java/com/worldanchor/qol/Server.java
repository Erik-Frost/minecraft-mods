package com.worldanchor.qol;

import com.mojang.brigadier.Command;
import com.worldanchor.qol.mixin.ServerLevelAccessor;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Random;

public class Server implements DedicatedServerModInitializer {
    public static final String MODID = "world-anchor-qol";

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(Commands.literal("spawn")
                    .requires(source -> source.hasPermission(0))
                            .executes(context -> {
                                CommandSourceStack source = context.getSource();
                                teleportPlayerWithMountAndPassengers(source.getLevel().getSharedSpawnPos(),
                                        source.getPlayerOrException(), source.getLevel(), source.getServer().overworld());
                                return Command.SINGLE_SUCCESS;
                            })
            );
            dispatcher.register(Commands.literal("home")
                    .requires(source -> source.hasPermission(0))
                    .executes(context -> {
                        CommandSourceStack source = context.getSource();
                        if (source.getPlayerOrException().getRespawnPosition() != null) {
                            teleportPlayerWithMountAndPassengers(source.getPlayerOrException().getRespawnPosition().above(1),
                                    source.getPlayerOrException(), source.getLevel(),
                                    source.getServer().getLevel(source.getPlayerOrException().getRespawnDimension()));
                        }
                        return Command.SINGLE_SUCCESS;
                    })
            );
        });
    }

    private void teleportPlayerWithMountAndPassengers(BlockPos target, ServerPlayer player, ServerLevel world,
            ServerLevel dimension){
        Entity vehicle = player.getVehicle();
        List<Entity> passengers = player.getPassengers();
        Random random = new Random();
        // Spawn particles at old location
        ClientboundLevelParticlesPacket particleS2CPacket = new ClientboundLevelParticlesPacket(
                ParticleTypes.PORTAL, true, player.getX(), player.getY() + random.nextDouble() * 2.0D, player.getZ(),
                (float)random.nextGaussian(), 0.0f, (float)random.nextGaussian(), 1, 100);
        for(int i = 0; i < world.players().size(); ++i) {
            ((ServerLevelAccessor)world).sendParticles(world.players().get(i), true, player.getX(), player.getY(), player.getZ(), particleS2CPacket);
        }
        world.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + random.nextDouble() * 2.0D, player.getZ(), 100, random.nextGaussian(), 0.0D, random.nextGaussian(), 1);
        // Teleport player to world spawn
        player.teleportTo(dimension,
                target.getX(), target.getY(), target.getZ(),
                player.getYRot(), player.getXRot());
        // Teleport vehicle to world spawn with player
        if (vehicle != null) {
            vehicle.changeDimension(dimension);
            vehicle.teleportTo(target.getX(), target.getY(), target.getZ());
        }
        // Teleport passengers to world spawn with player
        for (Entity passenger : passengers) {
            passenger.changeDimension(dimension);
            passenger.teleportTo(target.getX(), target.getY(), target.getZ());
        }
        // Spawn particles at new location
        ClientboundLevelParticlesPacket particleS2CPacket2 = new ClientboundLevelParticlesPacket(
                ParticleTypes.PORTAL, true, target.getX(), target.getY() + random.nextDouble() * 2.0D, target.getZ(),
                (float)random.nextGaussian(), 0.0f, (float)random.nextGaussian(), 1, 100);
        for(int i = 0; i < world.players().size(); ++i) {
            ((ServerLevelAccessor)world).sendParticles(world.players().get(i), true, target.getX(), target.getY(), target.getZ(), particleS2CPacket2);
        }
    }

}


