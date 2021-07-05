package com.worldanchor.qol.mixin;


import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    // Adds coordinates to death messages
    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageTracker;getDeathMessage()Lnet/minecraft/text/Text;"))
    Text redirectMethod(DamageTracker damageTracker) {
        BlockPos deathPos = ((ServerPlayerEntity)(Object)this).getBlockPos();
        TranslatableText deathMessage = (TranslatableText) damageTracker.getDeathMessage();
        deathMessage.append(" at [x=%d, y=%d, z=%d]".formatted(deathPos.getX(), deathPos.getY(), deathPos.getZ()));
        return deathMessage;
    }

}
