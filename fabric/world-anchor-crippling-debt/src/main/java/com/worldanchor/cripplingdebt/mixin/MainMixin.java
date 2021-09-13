package com.worldanchor.cripplingdebt.mixin;

import net.minecraft.server.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Main.class)
public class MainMixin {
    @ModifyArg(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelSettings;<init>(Ljava/lang/String;Lnet/minecraft/world/level/GameType;ZLnet/minecraft/world/Difficulty;ZLnet/minecraft/world/level/GameRules;Lnet/minecraft/world/level/DataPackConfig;)V"), index = 2)
    private static boolean injected(boolean x) {return true;}

}