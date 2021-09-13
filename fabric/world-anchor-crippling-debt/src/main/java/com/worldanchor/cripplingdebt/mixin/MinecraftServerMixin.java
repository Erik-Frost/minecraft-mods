package com.worldanchor.cripplingdebt.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Redirect(method = "setDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;isHardcore()Z"))
    private boolean redirectIsHardcore(WorldData worldData) {
        return false;
    }

}
