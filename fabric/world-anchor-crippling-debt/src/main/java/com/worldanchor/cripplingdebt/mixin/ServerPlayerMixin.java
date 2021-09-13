package com.worldanchor.cripplingdebt.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Shadow public abstract boolean setGameMode(GameType gameType);

    @Inject(method = "die", at = @At(value = "TAIL"))
    private void inject(DamageSource damageSource, CallbackInfo ci) {
        setGameMode(GameType.SPECTATOR);
    }

}
