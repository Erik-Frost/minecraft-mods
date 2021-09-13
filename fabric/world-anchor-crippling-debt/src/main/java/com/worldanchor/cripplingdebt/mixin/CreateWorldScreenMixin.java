package com.worldanchor.cripplingdebt.mixin;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @ModifyArg(method = "onCreate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelSettings;<init>(Ljava/lang/String;Lnet/minecraft/world/level/GameType;ZLnet/minecraft/world/Difficulty;ZLnet/minecraft/world/level/GameRules;Lnet/minecraft/world/level/DataPackConfig;)V"), index = 2)
    private boolean injected(boolean x) {
        return true;
    }

}