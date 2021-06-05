package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.AbstractTeam;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow @Nullable public abstract AbstractTeam getScoreboardTeam();

    @Inject(method = "writeNbt", at = @At("HEAD"))
    void injectMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (getScoreboardTeam() != null) {
            nbt.putString("Team", getScoreboardTeam().getName());
        }
    }
}
