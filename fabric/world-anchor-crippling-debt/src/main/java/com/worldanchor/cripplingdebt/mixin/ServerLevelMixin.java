package com.worldanchor.cripplingdebt.mixin;

import com.worldanchor.cripplingdebt.Mod;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Redirect(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"))
    private void injected(ServerLevel serverLevel, long l) {
        //night is from 13000 to 23000
        if (13000 <= serverLevel.getLevelData().getDayTime() % 24000 && serverLevel.getLevelData().getDayTime() % 24000 < 23000)
            serverLevel.setDayTime(serverLevel.getLevelData().getDayTime() + serverLevel.getGameRules().getRule(Mod.NIGHT_DURATION_DIVIDER).get());
        else serverLevel.setDayTime(serverLevel.getLevelData().getDayTime() + serverLevel.getGameRules().getRule(Mod.DAY_DURATION_DIVIDER).get());
    }

}