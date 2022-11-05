package com.subjectdeltav.spiritw.effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Cureall extends InstantenousMobEffect{

	public Cureall() {
		super(MobEffectCategory.BENEFICIAL, 2039587);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void applyEffectTick(LivingEntity mob, int x)
	{
		mob.removeAllEffects();
	}

}
