package com.subjectdeltav.spiritw.events;

import com.subjectdeltav.spiritw.init.EffectInit;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WoundedProtectionHandler {
	//protect players from damage or kills from certain sources
	@SubscribeEvent
	public void TakeDamage(LivingHurtEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			DamageSource src = event.getSource();
			if(player.hasEffect(EffectInit.WOUNDED.get()))
			{
				System.out.println("Wounded Player has taken damage, checking damage type");
				if(
						src == DamageSource.FALLING_BLOCK || 
						src == DamageSource.HOT_FLOOR || 
						src == DamageSource.LIGHTNING_BOLT ||
						src.isMagic() ||
						src.isBypassMagic())
				{
					System.out.println("Wounded Player is Immune to Damage of type " + src.toString());
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void woundedDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			DamageSource src = event.getSource();
			if(player.hasEffect(EffectInit.WOUNDED.get()))
			{
				System.out.println("Wounded Player has died, checking for immunities...");
				if(src == DamageSource.GENERIC || src.isProjectile())
				{
					System.out.println("Wounded Players cannot be killed by Arrows or Zombie Attacks, Cancelling...");
					event.setCanceled(false);
				}else
				{
					System.out.println("Wounded Players are NOT immune to damage of type " + src.toString());
				}
				
			}
		}
	}
	
	@SubscribeEvent
	public void heal(LivingHealEvent event)
	{
		if(event.getEntity() instanceof Player && event.getEntity().hasEffect(EffectInit.WOUNDED.get()))
		{
			System.out.println("Wounded Players cannot heal, cancelling...");
			event.setCanceled(true);
		}
	}

}
