package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggItemMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;spawnFromItemStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"))
    Entity redirectUseSpawnMethod(EntityType entityType, ServerWorld serverWorld, ItemStack stack, PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        Entity entity = entityType.spawnFromItemStack(serverWorld, stack, player, pos, SpawnReason.SPAWN_EGG, false, false);
        if (entity instanceof LivingEntity && player.getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, player.getScoreboardTeam().getName());
        }
        return entity;
    }

    @Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;spawnFromItemStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"))
    Entity redirectUseOnBlockSpawnMethod(EntityType entityType, ServerWorld serverWorld, ItemStack stack, PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        Entity entity = entityType.spawnFromItemStack(serverWorld, stack, player, pos, SpawnReason.SPAWN_EGG, false, false);
        if (entity instanceof LivingEntity && player.getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, player.getScoreboardTeam().getName());
        }
        return entity;
    }

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/MobSpawnerBlockEntity;getLogic()Lnet/minecraft/world/MobSpawnerLogic;"))
    void injectChangeSpawnerTeam(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        if (world instanceof ServerWorld) {
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.SPAWNER)) {
                BlockEntity blockEntity = world.getBlockEntity(blockPos);
                if (blockEntity instanceof MobSpawnerBlockEntity) {
                    MobSpawnerLogic mobSpawnerLogic = ((MobSpawnerBlockEntity)blockEntity).getLogic();
                    if (((MobSpawnerLogicAccessor) mobSpawnerLogic).getMobSpawnEntry().getEntityNbt().contains("Team")) {
                        ((MobSpawnerLogicAccessor) mobSpawnerLogic).getMobSpawnEntry().getEntityNbt().remove("Team");
                    }
                    if (context.getPlayer() != null && context.getPlayer().getScoreboardTeam() != null) {
                        ((MobSpawnerLogicAccessor) mobSpawnerLogic).getMobSpawnEntry().getEntityNbt().putString("Team", context.getPlayer().getScoreboardTeam().getName());
                    }

                    //blockEntity.markDirty();
                    //world.updateListeners(blockPos, blockState, blockState, 3);
                }
            }
        }
    }

}
