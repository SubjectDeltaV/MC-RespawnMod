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
 * Applied whever a player is revived from a downed state
 * either via Potion or Second Wind
 * Severity scales from just a few sympoms to ramping intensity
 * Lowest Levels just cause hunger
 * Highest Levels cause: Hunger, Weakness, Miners Fatigue, Blindness, Confusion, and a movment debuff
 * @author Mount
 *
 */
public class Revival_Sickness extends MobEffect 
{

	//This effect is for when a player is revived from a downed state (via potion or second wind)
	
	public Revival_Sickness() 
	{
		super(MobEffectCategory.HARMFUL, 2039587);
		hunger = MobEffects.HUNGER;
		weakness = MobEffects.WEAKNESS;
		mfatigue = MobEffects.DIG_SLOWDOWN;
		blindness = MobEffects.BLINDNESS;
		nausea = MobEffects.CONFUSION;
		slow = MobEffects.MOVEMENT_SLOWDOWN;
		//appliedOnce = false;
		//t = 0;
	}
	
	MobEffect hunger;
	MobEffect weakness;
	MobEffect mfatigue;
	MobEffect blindness;
	MobEffect nausea;
	MobEffect slow;
	//boolean appliedOnce;
	//int t; //ticks
	
	/**
	 * Applies effect every 30 Seconds
	 */
	@Override //apply effect every 30 seconds, related effects are applied just short of this to ensure they are re-applied
	public boolean isDurationEffectTick(int x, int y)
	{
		int k = 600;
		if(x % k == 0)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	/**
	 * Curative items include ONLY the CureAll, but this effect wears off over time
	 * Its related side effects can be temporarily relieved through milk but come back
	 * each time applyEffectTick() is called
	 */
	@Override //setup items that can cure (none)
	public List<ItemStack> getCurativeItems()
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		Potion potion = PotionInit.CUREALL_POTION.get();
		ItemStack devPotion = ItemStack.EMPTY;
		devPotion = PotionUtils.setPotion(devPotion, potion);
		list.add(devPotion);
		return list;
	}
	
	/**
	 * Apply Effects with graduating intensity based on Severity
	 * Severity is increased during consumption if the player
	 * already had this effect when they were downed
	 * 1 = Hunger
	 * 2 = Weakness
	 * 3 = Miners Fatigue
	 * 4 = blindness
	 * Further increases increase length of the effect
	 * There is no cap in severity at this time
	 */
	@Override //configure effects
	public void applyEffectTick(LivingEntity pl, int x)
	{
		pl.addEffect(new MobEffectInstance(hunger, 590, 5 * x));
		if(x >= 2)
		{
			pl.addEffect(new MobEffectInstance(weakness, 590, 4 * x));
			if( x>= 3)
			{
				pl.addEffect(new MobEffectInstance (mfatigue, 590, 2));
				if(x >= 4)
				{
					pl.addEffect(new MobEffectInstance (blindness, 590, x / 4));
				}
			}
		}
	}
}
