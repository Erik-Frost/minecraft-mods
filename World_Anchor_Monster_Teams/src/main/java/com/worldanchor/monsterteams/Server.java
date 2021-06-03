package com.worldanchor.monsterteams;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Server implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            if (minecraftServer.getGameRules().getBoolean(Mod.SET_UP_BASIC_TEAMS_NEXT_SERVER_START)) {
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team add default");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team add red");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team add orange");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team add yellow");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team add green");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team add blue");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team add purple");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team modify red color red");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team modify orange color gold");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team modify yellow color yellow");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team modify green color green");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team modify blue color aqua");
                minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "team modify purple color light_purple");
                minecraftServer.getGameRules().get(Mod.SET_UP_BASIC_TEAMS_NEXT_SERVER_START).set(false, minecraftServer);
            }
        });



        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("displayTeamNameTags")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(argument("bool", BoolArgumentType.bool())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            boolean displayTeamNameTags = BoolArgumentType.getBool(context, "bool");
                            // Change all current mob nametags
                            for (Team team : source.getMinecraftServer().getScoreboard().getTeams()) {
                                for (MobEntity mobEntity : TeamUtil.getMobEntitiesOnTeam(team, source.getMinecraftServer())) {
                                    mobEntity.setCustomNameVisible(displayTeamNameTags);
                                }
                            }
                            //Set corresponding gamerule
                            source.getMinecraftServer().getGameRules().get(Mod.DISPLAY_TEAM_NAME_TAGS).set(displayTeamNameTags ,source.getMinecraftServer());
                            //Send feedback
                            if (displayTeamNameTags) source.sendFeedback(new TranslatableText(
                                    "chat.feedback.command.display_team_name_tags.true"), true);
                            else source.sendFeedback(new TranslatableText(
                                    "chat.feedback.command.display_team_name_tags.false"), true);
                            return Command.SINGLE_SUCCESS;
                    }))
            );

        });
    }
}
