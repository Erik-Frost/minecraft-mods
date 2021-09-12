package com.worldanchor.cripplingdebt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.level.GameRules;

public class Mod implements ModInitializer {
    public static final String MODID = "world-anchor-crippling-debt";

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
            minecraftServer.getGameRules().getRule(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE).set(200, minecraftServer);
        });
    }
}
