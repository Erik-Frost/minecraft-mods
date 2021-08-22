package com.worldanchor.monsterteams.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow @Nullable public abstract Team getTeam();

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    void injectMethod(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (getTeam() != null) {
            tag.putString("Team", getTeam().getName());
        }
    }
}
