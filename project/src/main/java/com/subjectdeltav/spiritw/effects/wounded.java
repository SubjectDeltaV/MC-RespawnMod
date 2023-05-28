package com.subjectdeltav.spiritw.effects;

import java.util.ArrayList;
import java.util.List;

import com.subjectdeltav.spiritw.init.PotionInit;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

/**
 * This Effect is the "downed" state that the player goes into when they die
 * application of the wounded effect is handled by the DeathHandler event handler
 * upkeep of this effect is handled by the WoundedProtectionHandler event handler
 * NOTE: The TIMER for this effect is the players food bar
 * @author Mount
 *
 */
public class wounded extends MobEffect 
{
	
	public wounded() 
	{
		super(MobEffectCategory.HARMFUL, 2039587);
		//t = 200;
		//player = null;
	}
	//x is severity

	//int t; // where t is time in ticks
	MobEffectInstance dark = new MobEffectInstance(MobEffects.BLINDNESS, 6000);
	MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 5);
	MobEffectInstance hunger = new MobEffectInstance(MobEffects.HUNGER, 6000, 30); 
	//public LivingEntity attacker;
	//public boolean diedFromMob = false;
	//public Player player;
	
	@Override
	public boolean isDurationEffectTick(int x, int y)
	{
		//set frequency of application to every tick
		return true;
	}
	
	/**
	 * Curative Items include the REVIVE POTION only
	 * You can also relieve this effect by killing the enemy that downed you
	 * (Logic for which is handled by one of the event handlers)
	 */
	@Override //setup items that can cure
	public List<ItemStack> getCurativeItems()
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		Potion potion = PotionInit.REVIVE_POTION.get();
		ItemStack revivePotion = ItemStack.EMPTY;
		revivePotion = PotionUtils.setPotion(revivePotion, potion);
		list.add(revivePotion);
		return list;
	}
	
	/**
	 * Logic for apply effects
	 * Effects should be applied every tick
	 * Effects directly include Darkness, Slowness, and Hunger
	 * While there is a severity for this effect, it does not affect the intesity of the symptons/related effects
	 * Instead the intensity of this effect is directly passed on to the revival sickness effect
	 */
	@Override
	public void applyEffectTick(LivingEntity pl, int x)
	{
		
		//t--; //reduce the timer by 1 every tick
		if(pl.hasEffect(MobEffects.DARKNESS) == false)
		{
			pl.addEffect(dark);
		}
		if(pl.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) == false)
		{
			pl.addEffect(slow);
		}
		if(pl.hasEffect(MobEffects.HUNGER) == false)
		{
			pl.addEffect(hunger);
		}
	}
}
