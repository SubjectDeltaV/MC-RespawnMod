package com.subjectdeltav.spiritw.events;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.wounded;
import com.subjectdeltav.spiritw.init.EffectInit;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DeathHandler 
{
	//public MobEffectInstance downedplayer = new MobEffectInstance(EffectInit.WOUNDED.get(), 1800);
	//boolean diedAlready = false;
	
	@SubscribeEvent
	public void Death(LivingDeathEvent event) 
	{
		System.out.println("Death of LivingEntity Detected, checking if Player...");
		//MobEffectInstance downedplayer = new MobEffectInstance(EffectInit.WOUNDED.get(), 1800);
		if(event.getEntity() instanceof Player)
		{
			Player pl = (Player) event.getEntity();
			DamageSource src = event.getSource();
			System.out.println("A Player has died, checking for wounded status...");
			//MobEffectInstance playerEffect = pl.getEffect(EffectInit.WOUNDED.get());
			if(
					src == DamageSource.DROWN || 
					src == DamageSource.IN_WALL || 
					src == DamageSource.LAVA || 
					src == DamageSource.OUT_OF_WORLD ||
					src == DamageSource.STARVE)
			{
				System.out.println("No wounded status from Drowning, Suffocation, Lava, Falling out of World, or Starvation, Ignoring...");
			}else if(pl.hasEffect(EffectInit.WOUNDED.get()))
			{
				System.out.println("Player was in a wounded state, the event will not be cancelled");
				src.setIsFall();
			} else 
			{
				System.out.println("Player wasn't wounded when they died, puting into wounded state and cancelling event");
				pl.setForcedPose(Pose.DYING);
				src.getLocalizedDeathMessage(pl);
				event.setCanceled(true);
				pl.setHealth(1);
				pl.addEffect(new MobEffectInstance(EffectInit.WOUNDED.get(), 1800));
			}
		}
	}

}
