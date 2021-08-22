package com.worldanchor.monsterteams.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Slime.class)
public interface SlimeInvoker {
    @Invoker("isDealsDamage")
    public boolean invokeIsDealsDamage();

    @Invoker("dealDamage")
    public void invokeDealDamage(LivingEntity livingEntity);


}
