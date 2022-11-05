package com.subjectdeltav.spiritw.events;

import java.util.HashMap;
import java.util.Map;

import com.subjectdeltav.spiritw.effects.ModEffects;
import com.subjectdeltav.spiritw.init.EffectInit;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DeathHandler 
{
	//This handler is for reacting to deaths, it currently initiates the wounded status effect and allows for "second wind"
	
	//public MobEffectInstance downed player = new MobEffectInstance(EffectInit.WOUNDED.get(), 1800);
	//boolean diedAlready = false;
	private Map<String, String> rememberKillers = new HashMap<String, String>();
	private Map<String, Vec3> rememberDeath = new HashMap<String, Vec3>();
	
	@SubscribeEvent
	public void Death(LivingDeathEvent event) 
	{
		System.out.println("Death of LivingEntity Detected, checking if Player...");

		//MobEffectInstance downedplayer = new MobEffectInstance(EffectInit.WOUNDED.get(), 1800);
		//boolean diedFromMob;
		LivingEntity attacker = null;
		Player pl = null;
		if(event.getEntity() instanceof Player)
		{
			pl = (Player) event.getEntity();
			DamageSource src = event.getSource();
			int woundseverity;
			if(pl.hasEffect(EffectInit.REVIVAL_SICKNESS.get()))
			{
				MobEffectInstance sickness = pl.getEffect(EffectInit.REVIVAL_SICKNESS.get());
				woundseverity = sickness.getAmplifier() + 1;
			}else
			{
				woundseverity = 0;
			}
			if(src.getEntity() != null && src.getEntity() instanceof Mob)
			{
				System.out.println("Player died from Mob, recording mob info");
				Entity ent = src.getEntity();
				attacker = (LivingEntity) ent;
				//diedFromMob = true;
				//int mobId = pl.getLastHurtByMob().getId();
				rememberKillers.put(pl.getLastHurtByMob().getEncodeId(), pl.getEncodeId());
			} else
			{
				attacker = null;
				//diedFromMob = false;
			}
			System.out.println("A Player has died, checking for wounded status...");
			//MobEffectInstance playerEffect = pl.getEffect(EffectInit.WOUNDED.get());
			if(
					src == DamageSource.DROWN || 
					src == DamageSource.IN_WALL || 
					src == DamageSource.LAVA || 
					src == DamageSource.OUT_OF_WORLD ||
					src == DamageSource.STARVE)
			{
				System.out.println("No wounded status from Drowning, Suffocation, Lava, Falling out of World, or Starvation, Ghost Effect will be initiated on respawn");
				rememberDeath.put(pl.getStringUUID(), pl.position());
				
			}else if(pl.hasEffect(EffectInit.WOUNDED.get()))
			{
				System.out.println("Player was in a wounded state, the event will not be cancelled");
				src.setIsFall();
			}else if(pl.hasEffect(ModEffects.ENTER_GHOST_STATE))
			{
				System.out.println("Lantern has this player marked as starting a spirit walk, player will enter ghost state on respawn");
				rememberDeath.put(pl.getStringUUID(), pl.position());
			} else 
			{
				System.out.println("Player wasn't wounded when they died, puting into wounded state and cancelling event");
				pl.setForcedPose(Pose.DYING);
				src.getLocalizedDeathMessage(pl);
				event.setCanceled(true);
				pl.setHealth(1);
				if(woundseverity == 0)
				{
					pl.addEffect(new MobEffectInstance(EffectInit.WOUNDED.get(), 6000, 1));
					System.out.println("Wounded Amplifier is set to 1");
				}else
				{
					System.out.println("Wounded Amplifier is set to " + woundseverity);
					pl.addEffect(new MobEffectInstance(EffectInit.WOUNDED.get(), 6000, woundseverity));
				}
			}
		}
		else if(event.getEntity() instanceof Mob)
		{
			System.out.println("Mob has died, checking to see if it killed any players");
			LivingEntity mob = event.getEntity();
			LivingEntity killer = mob.getLastHurtByMob();
			if(rememberKillers.containsKey(mob.getEncodeId()))
			{
				String mobId = String.valueOf(mob.getEncodeId());
				System.out.println("Mob has killed players, checking if killer was the original victim");
				if(killer.getEncodeId() == rememberKillers.get(mobId) && killer.hasEffect(EffectInit.WOUNDED.get()));
				{
					System.out.println("Mob was killed by his victim while it was downed, lifting wounded status");
					MobEffectInstance woundEffect = killer.getEffect(EffectInit.WOUNDED.get());
					int woundEffectAmplifier = woundEffect.getAmplifier();
					if(killer != null) //to prevent CTD
					{
						killer.removeAllEffects();
						MobEffectInstance sickness = new MobEffectInstance(EffectInit.REVIVAL_SICKNESS.get(), 2400, woundEffectAmplifier);
						killer.addEffect(sickness);
					    rememberKillers.remove(mobId);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void Respawn(PlayerEvent.Clone event)
	{
		Player player = event.getEntity();
		if(event.isWasDeath() && rememberDeath.containsKey(player.getStringUUID()))
		{
			Vec3 vec = rememberDeath.get(player.getStringUUID()); //get death loc
			player.teleportTo(vec.x, vec.y, vec.z); //teleport player to there death
			player.addEffect(new MobEffectInstance(ModEffects.GHOST, 35000)); //put into ghost state
		}
	}

}
