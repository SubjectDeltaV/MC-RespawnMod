package com.subjectdeltav.spiritw.effects;

import com.subjectdeltav.spiritw.SpiritwMobEffect;
import com.subjectdeltav.spiritw.init.EffectInit;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class wounded extends SpiritwMobEffect 
{
	public wounded() 
	{
		super(MobEffectCategory.HARMFUL, 2039587);
		t = 1800;
	}
	//x is severity

	int t; // where t is time in ticks
	MobEffectInstance dark = new MobEffectInstance(MobEffects.DARKNESS, 5);
	MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 5);
	
	@Override
	public boolean isDurationEffectTick(int x, int y)
	{
		
		return true;
	}
	
	@Override
	public void applyEffectTick(LivingEntity mob, int x)
	{
		t--; //reduce the timer by 1 every tick
		if(mob.hasEffect(MobEffects.DARKNESS) == false)
		{
			mob.addEffect(dark);
		}
		if(mob.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) == false)
		{
			mob.addEffect(slow);
		}
		if(t<3)
		{
			System.out.println(mob.getName() + " has bleed out");
			mob.kill(); //kill the player once the timer runs out
			mob.removeEffect(EffectInit.WOUNDED.get());
		}
	}
}
