package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public abstract class MonsterMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    void injectInitAddition(EntityType<? extends Monster> entityType, Level level, CallbackInfo ci) {
        if (((Monster) (Object) this).getType() != EntityType.CREEPER) {
            ((MobAccessor) this).getTargetSelector().addGoal(4,
                    new NearestAttackableTargetGoal<>((Mob) (Object) this, LivingEntity.class,
                            5,false, false,
                            new TeamUtil.OnTeamAndHostileExceptCreeperEntityPredicate()));
        }

    }

    @Inject(method = "shouldDropLoot", at = @At("RETURN"), cancellable = true)
    private void injectShouldDropLoot(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && ((Monster) (Object) this).getTeam() == null);
    }
}
