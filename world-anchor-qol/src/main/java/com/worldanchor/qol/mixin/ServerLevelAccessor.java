package com.worldanchor.qol.mixin;



import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public interface ServerLevelAccessor {

    @Invoker("sendParticles")
    boolean sendParticles(ServerPlayer serverPlayer, boolean bl, double d, double e, double f, Packet<?> packet);

}
