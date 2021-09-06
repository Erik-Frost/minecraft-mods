package com.worldanchor.playertrust;

import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("world-anchor-player-trust")
public class WorldAnchorMod
{

    public WorldAnchorMod() {
        // This statement must appear first, failing to include
        // it will cause a runtime error
        MixinBootstrap.init();

        // Retrieves the DEFAULT mixin environment and registers
        // the config file
        MixinEnvironment.getDefaultEnvironment()
                .addConfiguration("world-anchor-player-trust.mixins.json");
    }



}
