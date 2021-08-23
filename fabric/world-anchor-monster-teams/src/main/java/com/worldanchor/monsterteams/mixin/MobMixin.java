package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(Mob.class)
public abstract class MobMixin {


    @Shadow @Final protected GoalSelector targetSelector;

    @Redirect(method = "convertTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    boolean redirectMethod(Level level, Entity entity) {
        if (((Mob) (Object) this).getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((Mob) (Object) this).getTeam().getName());
        }
        return level.addFreshEntity(entity);
    }

    @Redirect(method = "checkAndHandleImportantInteractions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/SpawnEggItem;spawnOffspringFromSpawnEgg(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;"))
    Optional<Mob> redirectMethod(SpawnEggItem spawnEggItem, Player user, Mob mobEntity, EntityType<? extends Mob> entityType, ServerLevel serverLevel, Vec3 vec3, ItemStack itemStack) {
        Optional<Mob> optional = spawnEggItem.spawnOffspringFromSpawnEgg(user, mobEntity, entityType, serverLevel, vec3, itemStack);
        if (optional.isPresent() && optional.get() instanceof LivingEntity && user.getTeam() != null) {
            TeamUtil.addToTeamHelper(optional.get(), user.getTeam().getName());
        }
        return optional;
    }

}
