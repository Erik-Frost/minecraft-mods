package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.class)
public abstract class SlimeMixin {

    @Redirect(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    boolean redirectMethod(Level level, Entity entity) {
        if (((Slime) (Object) this).getTeam() != null) {
            TeamUtil.addToTeamHelper((LivingEntity) entity, ((Slime) (Object) this).getTeam().getName());
        }
        return level.addFreshEntity(entity);
    }


    @Redirect(method = "dealDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;isAlive()Z"))
    boolean redirectDamageMethod(Slime Slime, LivingEntity target) {
        if (((Slime) (Object) this).getTeam() != null
                && target.getTeam() == ((Slime) (Object) this).getTeam()) {
            return false;
        }
        else return Slime.isAlive();
    }

    @Inject(method = "push", at = @At(value = "TAIL"))
    void redirectDamageMethod(Entity entity, CallbackInfo ci) {
        if (((SlimeInvoker) this).invokeIsDealsDamage() && entity instanceof LivingEntity
                && ((entity.getTeam() != ((Slime) (Object) this).getTeam() && ((entity instanceof Monster && !(entity instanceof Creeper)) || entity instanceof Slime))
                    || (entity.getTeam() != ((Slime) (Object) this).getTeam() && entity.getTeam() != null))) {
            ((SlimeInvoker)this).invokeDealDamage((LivingEntity)entity);
        }
    }
}
