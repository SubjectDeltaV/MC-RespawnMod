package com.subjectdeltav.spiritw.effects;

import com.subjectdeltav.spiritw.init.EffectInit;
import com.subjectdeltav.spiritw.init.PotionInit;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

public class Revive extends InstantenousMobEffect
{

	public Revive() 
	{
		super(MobEffectCategory.BENEFICIAL, 2039587);
	}
	
	@Override
	public void applyEffectTick(LivingEntity mob, int x)
	{
		if(mob.hasEffect(EffectInit.WOUNDED.get()))
		{
			//ItemStack cure = ItemStack.EMPTY;
			//cure = PotionUtils.setPotion(cure, PotionInit.REVIVE_POTION.get());
			mob.removeEffect(EffectInit.WOUNDED.get());
			mob.removeEffect(MobEffects.HUNGER);
			mob.removeEffect(MobEffects.DARKNESS);
		}
	}

}
