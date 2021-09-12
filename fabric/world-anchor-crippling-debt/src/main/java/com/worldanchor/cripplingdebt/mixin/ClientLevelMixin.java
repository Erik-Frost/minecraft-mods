package com.worldanchor.cripplingdebt.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Redirect(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;setDayTime(J)V"))
    private void injected(ClientLevel clientLevel, long l) {
        if (13000 <= clientLevel.getLevelData().getDayTime() % 24000 && clientLevel.getLevelData().getDayTime() % 24000 < 23000)
            clientLevel.setDayTime(clientLevel.getLevelData().getDayTime() + 3L);
        else clientLevel.setDayTime(clientLevel.getLevelData().getDayTime() + 2L);
    }

}