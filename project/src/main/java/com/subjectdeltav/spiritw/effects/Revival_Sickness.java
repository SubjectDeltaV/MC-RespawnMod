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

public class Revival_Sickness extends MobEffect {

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
