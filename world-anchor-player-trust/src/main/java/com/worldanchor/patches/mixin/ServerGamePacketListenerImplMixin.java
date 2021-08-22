package com.worldanchor.patches.mixin;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    // Removes anticheat on player movement
    @Redirect(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isChangingDimension()Z"))
    boolean redirectMethod(ServerPlayer serverPlayer) {
        return true;
    }

    // Removes anticheat on vehicle movement
    @Redirect(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"))
    boolean redirectMethod2(ServerGamePacketListenerImpl serverGamePacketListener) {
        return true;
    }
    @Redirect(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z"))
    boolean redirectMethod3(ServerLevel serverLevel, Entity entity, AABB aABB) {
        return false;
    }
    @ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue = 0.0625D))
    private double injected(double value) {
        return Double.MAX_VALUE;
    }



}
