package com.subjectdeltav.spiritw.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ResurrectionSickness extends MobEffect
{
	//The Effect a player gets after a successful spirit walk	
	//properties
	int freq = 3; //how often to apply, in seconds
	boolean applyTempEffects;
	MobEffect hunger = MobEffects.HUNGER;
	MobEffect fatigue = MobEffects.DIG_SLOWDOWN;
	MobEffectInstance nausea = new MobEffectInstance(MobEffects.CONFUSION, 600);
	MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600);
	
	//Constructor
	public ResurrectionSickness() 
	{
		super(MobEffectCategory.HARMFUL, 2039587);
		applyTempEffects = true;
	}

	//Overrode Methods
	@Override //apply effect every [freq] seconds, related effects are applied just short of this to ensure they are re-applied
	public boolean isDurationEffectTick(int x, int y)
	{
		int k = freq * 20;
		if(x % k == 0)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	@Override //configure effects, this is applied every [freq] seconds
	public void applyEffectTick(LivingEntity ent, int x)
	{
		if(applyTempEffects)
		{
			ent.addEffect(nausea);
			ent.addEffect(slow);
			applyTempEffects = false;
		}
		ent.addEffect(new MobEffectInstance(hunger, 100));
		ent.addEffect(new MobEffectInstance(fatigue, 100));
	}
}
