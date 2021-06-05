package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {


    @Shadow @Final protected GoalSelector targetSelector;

    @Redirect(method = "convertTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    boolean redirectMethod(World world, Entity entity) {
        if (((MobEntity) (Object) this).getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((MobEntity) (Object) this).getScoreboardTeam().getName());
        }
        return world.spawnEntity(entity);
    }

    @Redirect(method = "interactWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/SpawnEggItem;spawnBaby(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/EntityType;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;"))
    Optional<MobEntity> redirectMethod(SpawnEggItem spawnEggItem, PlayerEntity user, MobEntity mobEntity, EntityType<? extends MobEntity> entityType, ServerWorld serverWorld, Vec3d vec3d, ItemStack itemStack) {
        Optional<MobEntity> optional = spawnEggItem.spawnBaby(user, mobEntity, entityType, serverWorld, vec3d, itemStack);
        if (optional.isPresent() && optional.get() instanceof LivingEntity && user.getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper(optional.get(), user.getScoreboardTeam().getName());
        }
        return optional;
    }

}
