package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    void injectMethod(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        CompoundTag tag = ((PlayerManager) (Object) this).loadPlayerData(player);
        // If tag is null, it is the first time player has joined
        if (tag == null) {
            TeamUtil.addToTeamHelper(player, "default");
        }
    }

}
