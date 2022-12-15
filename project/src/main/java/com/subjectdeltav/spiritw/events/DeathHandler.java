package com.subjectdeltav.spiritw.events;

import java.util.HashMap;
import java.util.Map;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.ModEffects;
import com.subjectdeltav.spiritw.init.EffectInit;
import com.subjectdeltav.spiritw.init.ItemInit;
import com.subjectdeltav.spiritw.item.SpLantern;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import de.maxhenkel.corpse.Main;
import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathManager;
import de.maxhenkel.corpse.corelib.death.PlayerDeathEvent;
import de.maxhenkel.corpse.entities.CorpseEntity;

public class DeathHandler 
{
	//This handler is for reacting to deaths, it currently initiates the wounded status effect and allows for "second wind"
	
	//public MobEffectInstance downed player = new MobEffectInstance(EffectInit.WOUNDED.get(), 1800);
	//boolean diedAlready = false;
	private Map<String, String> rememberKillers = new HashMap<String, String>();
	private Map<String, BlockPos> rememberDeath = new HashMap<String, BlockPos>();
	private Map<String, Integer> rememberXP = new HashMap<String, Integer>(); 
	
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
				System.out.println("No wounded status from Drowning, Suffocation, Lava, Falling out of World, or Starvation, attempting to locate lantern to apply ghost effect");
				int xp = pl.totalExperience;
				Inventory plInv = pl.getInventory();
				HashMap hasLantern = ContainsLantern(plInv);
				if(hasLantern.containsKey(true))
				{
					spiritw.LOGGER.debug("Player has lantern. Using Lantern Functions to begin spiritwalk.");
					SpLantern lantern = (SpLantern) hasLantern.get(true);
					lantern.scanAndSaveItems(pl);
					lantern.DropCorpse(pl, true);
					event.setCanceled(true);
				}else
				{
					spiritw.LOGGER.debug("Player does not have lantern, unable to start spiritwalk. Player will need to respawn");
				}
			}else if(pl.hasEffect(EffectInit.WOUNDED.get()))
			{
				System.out.println("Player was in a wounded state, the event will not be cancelled");
				src.setIsFall();
			}
			else if(!pl.hasEffect(ModEffects.GHOST))
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
			} else
			{
				spiritw.LOGGER.debug("Player was in a spirit walk, the event will not be cancelled");
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
				if(killer == null) //prevent ctd
				{
					return;
				}
				if(killer.getEncodeId() == rememberKillers.get(mobId) && killer.hasEffect(EffectInit.WOUNDED.get()));
				{
					System.out.println("Mob was killed by his victim while it was downed, lifting wounded status");
					MobEffectInstance woundEffect = killer.getEffect(EffectInit.WOUNDED.get());
					if(killer != null && woundEffect != null) //to prevent CTD
					{
						int woundEffectAmplifier = woundEffect.getAmplifier();
						killer.removeAllEffects();
						MobEffectInstance sickness = new MobEffectInstance(EffectInit.REVIVAL_SICKNESS.get(), 2400, woundEffectAmplifier);
						killer.addEffect(sickness);
					    rememberKillers.remove(mobId);
					}
				}
			}
		}
	}
	
	//@SubscribeEvent
	public void Respawn(PlayerEvent.Clone event)
	{
		Player player = event.getEntity();
		Level level = player.level;
		spiritw.LOGGER.debug("Player respawn detected");
		if(event.isWasDeath() && rememberDeath.containsKey(player.getStringUUID()))
		{
			if(rememberXP.get(player.getStringUUID()) > 3) //make sure the player has enough xp to prevent instant death loops
			{
				spiritw.LOGGER.debug("Player has enough for a spiritwalk and was marked for such, teleporting to death");
				Player old = event.getOriginal();
				BlockPos pos = rememberDeath.get(player.getStringUUID()); //get death loc
				int xp = rememberXP.get(player.getStringUUID());
				player.giveExperiencePoints(xp);
				player.addEffect(new MobEffectInstance(ModEffects.GHOST, 35000)); //put into ghost state
				player.setPos(old.getX(), old.getY(), old.getZ());
				rememberDeath.remove(player.getStringUUID()); //remove the entry so players don't get stuck in a death loop
				rememberXP.remove(player.getStringUUID());
			}
		}
	}
	
	public boolean DropCorpse(Player player, DamageSource src)
	{
		Death death = Death.fromPlayer(player);
		PlayerDeathEvent deathEvent = new PlayerDeathEvent(death, (ServerPlayer) player, src);
		if(Main.SERVER_CONFIG.maxDeathAge.get() != 0)
		{
			deathEvent.storeDeath();
		}
		deathEvent.removeDrops();
		player.level.addFreshEntity(CorpseEntity.createFromDeath(player, death));
		
		new Thread(() -> deleteOldDeaths(deathEvent.getPlayer().getLevel())).start();
		
		return true;
	}
	
	public boolean SetGhostEffect(Player player, int xp)
	{
		player.addEffect(new MobEffectInstance(ModEffects.GHOST, 3600));
		player.giveExperiencePoints(xp);
		return true;
	}
	
	protected HashMap<Boolean, SpLantern> ContainsLantern(Inventory inv)
	{
		HashMap<Boolean, SpLantern> output = new HashMap<Boolean, SpLantern>();
		boolean keepChecking = true;
		while(keepChecking)
		{
			for(ItemStack itemStack : inv.items)
			{
				if(itemStack.getItem() instanceof SpLantern)
				{
					SpLantern lantern = (SpLantern) itemStack.getItem();
					output.put(true, lantern);
					keepChecking = false;
				}
			}
			keepChecking = false;
		}
		if(!output.containsKey(true))
		{
			output.put(false, null);
		}
		return output;
	}
	
	//Method copied from Corpse Mod
    public static void deleteOldDeaths(ServerLevel serverWorld) {
        int ageInDays = Main.SERVER_CONFIG.maxDeathAge.get();
        if (ageInDays < 0) {
            return;
        }
        long ageInMillis = ((long) ageInDays) * 24L * 60L * 60L * 1000L;

        DeathManager.removeDeathsOlderThan(serverWorld, ageInMillis);
    }

}
