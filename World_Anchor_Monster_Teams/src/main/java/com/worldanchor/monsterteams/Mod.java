package com.worldanchor.monsterteams;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;


public class Mod implements ModInitializer {

    public static final String MODID = "wolrd_anchor_monster_teams";

    //GameRules
    public static final GameRules.Key<GameRules.BooleanRule> DISPLAY_TEAM_NAME_TAGS =
            GameRuleRegistry.register("displayTeamNameTags", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> SET_UP_BASIC_TEAMS_NEXT_SERVER_START =
            GameRuleRegistry.register("setUpBasicTeamsNextServerStart", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {

    }
}
