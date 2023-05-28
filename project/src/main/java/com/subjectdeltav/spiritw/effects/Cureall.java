package com.subjectdeltav.spiritw.effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * Debug/Dev Tool Item that removes ALL Effects when drank
 * DO NOT ADD A CRAFTING RECIPE FOR THIS!
 * @author Mount
 *
 */
public class Cureall extends InstantenousMobEffect{

	public Cureall() {
		super(MobEffectCategory.BENEFICIAL, 2039587);
		//Auto-generated constructor stub
	}
	
	/**
	 * Removes all Effects when consumed
	 */
	@Override
	public void applyEffectTick(LivingEntity mob, int x)
	{
		mob.removeAllEffects();
	}

}
