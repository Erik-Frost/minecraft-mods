package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggItemMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;spawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/MobSpawnType;ZZ)Lnet/minecraft/world/entity/Entity;"))
    Entity redirectUseSpawnMethod(EntityType entityType, ServerLevel ServerLevel, ItemStack stack, Player player, BlockPos pos, MobSpawnType mobSpawnType, boolean alignPosition, boolean invertY) {
        Entity entity = entityType.spawn(ServerLevel, stack, player, pos, MobSpawnType.SPAWN_EGG, false, false);
        if (entity instanceof LivingEntity && player.getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, player.getTeam().getName());
        }
        return entity;
    }

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;spawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/MobSpawnType;ZZ)Lnet/minecraft/world/entity/Entity;"))
    Entity redirectUseOnBlockSpawnMethod(EntityType entityType, ServerLevel ServerLevel, ItemStack stack, Player player, BlockPos pos, MobSpawnType mobSpawnType, boolean alignPosition, boolean invertY) {
        Entity entity = entityType.spawn(ServerLevel, stack, player, pos, MobSpawnType.SPAWN_EGG, false, false);
        if (entity instanceof LivingEntity && player.getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, player.getTeam().getName());
        }
        return entity;
    }

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;getSpawner()Lnet/minecraft/world/level/BaseSpawner;"))
    void injectChangeSpawnerTeam(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        if (world instanceof ServerLevel) {
            BlockPos blockPos = context.getClickedPos();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(Blocks.SPAWNER)) {
                BlockEntity blockEntity = world.getBlockEntity(blockPos);
                if (blockEntity instanceof SpawnerBlockEntity) {
                    BaseSpawner baseSpawner = ((SpawnerBlockEntity)blockEntity).getSpawner();
                    if (((BaseSpawnerAccessor) baseSpawner).getNextSpawnData().getTag().contains("Team")) {
                        ((BaseSpawnerAccessor) baseSpawner).getNextSpawnData().getTag().remove("Team");
                    }
                    if (context.getPlayer() != null && context.getPlayer().getTeam() != null) {
                        ((BaseSpawnerAccessor) baseSpawner).getNextSpawnData().getTag().putString("Team", context.getPlayer().getTeam().getName());
                    }

                    //blockEntity.markDirty();
                    //world.updateListeners(blockPos, blockState, blockState, 3);
                }
            }
        }
    }

}
