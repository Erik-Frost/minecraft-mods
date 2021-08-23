package com.worldanchor.qol.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.CombatTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    // Adds coordinates to death messages
    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatTracker;getDeathMessage()Lnet/minecraft/network/chat/Component;"))
    Component redirectMethod(CombatTracker combatTracker) {
        BlockPos deathPos = ((ServerPlayer)(Object)this).blockPosition();
        TranslatableComponent deathMessage = (TranslatableComponent) combatTracker.getDeathMessage();
        deathMessage.append(" at [x=%d, y=%d, z=%d]".formatted(deathPos.getX(), deathPos.getY(), deathPos.getZ()));
        return deathMessage;
    }

}
