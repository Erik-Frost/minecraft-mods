package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin {

    @Redirect(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    boolean redirectMethod(World world, Entity entity) {
        if (((SlimeEntity) (Object) this).getScoreboardTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((SlimeEntity) (Object) this).getScoreboardTeam().getName());
        }
        return world.spawnEntity(entity);
    }


    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;isAlive()Z"))
    boolean redirectDamageMethod(SlimeEntity slimeEntity, LivingEntity target) {
        if (((SlimeEntity) (Object) this).getScoreboardTeam() != null
                && target.getScoreboardTeam() == ((SlimeEntity) (Object) this).getScoreboardTeam()) {
            return false;
        }
        else return slimeEntity.isAlive();
    }

    @Inject(method = "pushAwayFrom", at = @At(value = "TAIL"))
    void redirectDamageMethod(Entity entity, CallbackInfo ci) {
        if (((SlimeEntityAccessor) this).invokeCanAttack() && entity instanceof LivingEntity
                && ((entity.getScoreboardTeam() != ((SlimeEntity) (Object) this).getScoreboardTeam() && ((entity instanceof HostileEntity && !(entity instanceof CreeperEntity)) || entity instanceof SlimeEntity))
                    || (entity.getScoreboardTeam() != ((SlimeEntity) (Object) this).getScoreboardTeam() && entity.getScoreboardTeam() != null))) {
            ((SlimeEntityAccessor)this).invokeDamage((LivingEntity)entity);
        }
    }
}
