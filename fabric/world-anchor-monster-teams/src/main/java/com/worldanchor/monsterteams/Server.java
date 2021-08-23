package com.worldanchor.monsterteams;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.scores.Team;


public class Server implements DedicatedServerModInitializer {

    public static final String MODID = "world-anchor-monster-teams";

    //GameRules
    public static final GameRules.Key<GameRules.BooleanValue> DISPLAY_TEAM_NAME_TAGS =
            GameRuleRegistry.register("displayTeamNameTags", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> SET_UP_BASIC_TEAMS_NEXT_SERVER_START =
            GameRuleRegistry.register("setUpBasicTeamsNextServerStart", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));


    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            if (minecraftServer.getGameRules().getBoolean(SET_UP_BASIC_TEAMS_NEXT_SERVER_START)) {
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team add default");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team add red");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team add orange");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team add yellow");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team add green");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team add blue");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team add purple");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team modify red color red");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team modify orange color gold");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team modify yellow color yellow");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team modify green color green");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team modify blue color aqua");
                minecraftServer.getCommands().performCommand(minecraftServer.createCommandSourceStack(), "team modify purple color light_purple");
                minecraftServer.getGameRules().getRule(SET_UP_BASIC_TEAMS_NEXT_SERVER_START).set(false, minecraftServer);
            }
        });



        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(Commands.literal("displayTeamNameTags")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("bool", BoolArgumentType.bool())
                            .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            boolean displayTeamNameTags = BoolArgumentType.getBool(context, "bool");
                            // Change all current mob nametags
                            for (Team team : source.getServer().getScoreboard().getPlayerTeams()) {
                                for (Mob mob : TeamUtil.getMobEntitiesOnTeam(team, source.getServer())) {
                                    mob.setCustomNameVisible(displayTeamNameTags);
                                }
                            }
                            //Set corresponding gamerule
                            source.getServer().getGameRules().getRule(DISPLAY_TEAM_NAME_TAGS).set(displayTeamNameTags ,source.getServer());
                            //Send feedback
                            if (displayTeamNameTags) source.sendSuccess(new TranslatableComponent(
                                    "chat.feedback.command.display_team_name_tags.true"), true);
                            else source.sendSuccess(new TranslatableComponent(
                                    "chat.feedback.command.display_team_name_tags.false"), true);
                            return Command.SINGLE_SUCCESS;
                    }))
            );

        });
    }
}
