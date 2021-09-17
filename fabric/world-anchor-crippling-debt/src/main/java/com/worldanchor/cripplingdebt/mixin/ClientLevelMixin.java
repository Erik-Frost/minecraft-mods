package com.worldanchor.cripplingdebt.mixin;

import com.worldanchor.cripplingdebt.Mod;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Redirect(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;setDayTime(J)V"))
    private void injected(ClientLevel clientLevel, long l) {
        if (13000 <= clientLevel.getLevelData().getDayTime() % 24000 && clientLevel.getLevelData().getDayTime() % 24000 < 23000)
            clientLevel.setDayTime(clientLevel.getLevelData().getDayTime() + clientLevel.getGameRules().getRule(Mod.NIGHT_DURATION_DIVIDER).get());
        else clientLevel.setDayTime(clientLevel.getLevelData().getDayTime() + clientLevel.getGameRules().getRule(Mod.DAY_DURATION_DIVIDER).get());
    }

}