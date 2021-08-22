package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    void injectMethod(Connection connection, ServerPlayer player, CallbackInfo ci) {
        Tag tag = ((PlayerList) (Object) this).load(player);
        // If nbt is null, it is the first time player has joined
        if (tag == null) {
            TeamUtil.addToTeamHelper(player, "default");
        }
    }

}
