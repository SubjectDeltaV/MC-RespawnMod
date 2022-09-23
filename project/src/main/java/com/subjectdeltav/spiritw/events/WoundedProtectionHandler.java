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
						src == DamageSource.LAVA ||
						src == DamageSource.DROWN ||
						src == DamageSource.IN_WALL ||
						src == DamageSource.IN_FIRE ||
						src == DamageSource.OUT_OF_WORLD ||
						src == DamageSource.STARVE ||
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
	public void heal(LivingHealEvent event)
	{
		if(event.getEntity() instanceof Player && event.getEntity().hasEffect(EffectInit.WOUNDED.get()))
		{
			System.out.println("Wounded Players cannot heal, cancelling...");
			event.setCanceled(true);
		}
	}

}
