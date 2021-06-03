package com.worldanchor.monsterteams.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SlimeEntity.class)
public interface SlimeEntityAccessor {
    @Invoker("canAttack")
    public boolean invokeCanAttack();

    @Invoker("damage")
    public void invokeDamage(LivingEntity livingEntity);


}
