package com.subjectdeltav.spiritw.effects;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.subjectdeltav.spiritw.SpiritwMobEffect;
import com.subjectdeltav.spiritw.init.EffectInit;
import com.subjectdeltav.spiritw.init.PotionInit;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.alchemy.PotionUtils;

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
	MobEffectInstance dark = new MobEffectInstance(MobEffects.DARKNESS, 6000, 5);
	MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 5);
	MobEffectInstance hunger = new MobEffectInstance(MobEffects.HUNGER, 6000, 15); 
	//public LivingEntity attacker;
	//public boolean diedFromMob = false;
	//public Player player;
	
	@Override
	public boolean isDurationEffectTick(int x, int y)
	{
		//set frequency of application to every tick
		return true;
	}
	
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
