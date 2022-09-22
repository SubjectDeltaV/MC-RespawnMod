package com.subjectdeltav.spiritw.events;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.wounded;
import com.subjectdeltav.spiritw.init.EffectInit;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
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
		//MobEffectInstance downedplayer = new MobEffectInstance(EffectInit.WOUNDED.get(), 1800);
		if(event.getEntity() instanceof Player)
		{
			Player pl = (Player) event.getEntity();
			System.out.println("A Player has died, cancelling event and putting in down state");
			if(pl.hasEffect(EffectInit.WOUNDED.get()) == false)
			{
				event.setCanceled(true);
				pl.addEffect(new MobEffectInstance(EffectInit.WOUNDED.get(), 1800));
			}
		}
	}

}
