package com.worldanchor.cripplingdebt;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;

public class Mod implements ModInitializer {
    public static final String MODID = "world-anchor-crippling-debt";

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
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
}
