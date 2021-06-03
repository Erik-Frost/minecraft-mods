package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
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

    @Inject(method = "toTag", at = @At("HEAD"))
    void injectMethod(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (getScoreboardTeam() != null) {
            tag.putString("Team", getScoreboardTeam().getName());
        }
    }
}
