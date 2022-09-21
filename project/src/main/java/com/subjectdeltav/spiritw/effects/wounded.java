package com.subjectdeltav.spiritw.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class wounded extends MobEffect 
{
	protected wounded(MobEffectCategory HARMFUL, int x, int timeInput) 
	{
		super(HARMFUL, x);
		t = timeInput;
	}
	//x is severity

	int t; // where t is time in ticks
	MobEffectInstance dark = new MobEffectInstance(MobEffects.DARKNESS, 200);
	MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 5);
	
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
		}
	}
}
