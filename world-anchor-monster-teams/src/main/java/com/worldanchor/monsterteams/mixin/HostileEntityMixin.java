package com.worldanchor.monsterteams.mixin;

import com.worldanchor.monsterteams.TeamUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    void injectInitAddition(EntityType<? extends HostileEntity> entityType, World world, CallbackInfo ci) {
        if (((HostileEntity) (Object) this).getType() != EntityType.CREEPER) {
            ((MobEntityAccessor) this).getTargetSelector().add(4,
                    new TeamUtil.NearestAttackableTargetGoal<>((MobEntity) (Object) this, LivingEntity.class,
                            5,false, false,
                            new TeamUtil.OnTeamAndHostileExceptCreeperEntityPredicate()));
        }

    }

    @Inject(method = "shouldDropLoot", at = @At("RETURN"), cancellable = true)
    private void injectShouldDropLoot(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && ((HostileEntity) (Object) this).getScoreboardTeam() == null);
    }
}
