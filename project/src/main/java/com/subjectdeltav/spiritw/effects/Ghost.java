package com.subjectdeltav.spiritw.effects;

import java.util.ArrayList;
import java.util.List;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.init.PotionInit;
import com.subjectdeltav.spiritw.item.SpLantern;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class Ghost extends MobEffect
{
	//Effect for when you are Spirit Walking
	//Should drain XP and apply invisibility
	
	//Properties
	protected MobEffect blind;
	protected SpLantern lantern;
	protected int currentXP;
	protected int ticksRemaining;
	protected boolean drainXP;
	private final int effectLength = 10; //in seconds
	private final int freq = 3; //how often to apply, in seconds
	
	//Constructor
	public Ghost() 
	{
		super(MobEffectCategory.NEUTRAL, 2039587);
		blind = MobEffects.DARKNESS;
	}
	
	
	//Custom Methods
	
	public void setLantern(SpLantern lant)
	{
		this.lantern = lant;
	}
	
	public int estimateTicks(int playerXP) //estimate how many ticks the remaining XP has
	{
		this.currentXP = playerXP;
		this.ticksRemaining = playerXP * 6;
		spiritw.LOGGER.debug(ticksRemaining + " ticks left in ghost effect");
		return ticksRemaining;
	}
	
	private void addEffects(Player player)
	{
		player.addEffect(new MobEffectInstance(blind, effectLength * 20, 3));
		if(!player.isInvisible())
		{
			player.setInvisible(true);
		}
	}
	
	//Overrode Methods
	@Override //apply effect every 10 seconds, related effects are applied just short of this to ensure they are re-applied
	public boolean isDurationEffectTick(int x, int y)
	{
		int k = freq * 20;
		if(x % k == 0)
		{
			return true;
		}else
		{
			return false;
		}
	}
	
	@Override
	public List<ItemStack> getCurativeItems()
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		Potion potion = PotionInit.CUREALL_POTION.get();
		ItemStack devPotion = ItemStack.EMPTY;
		devPotion = PotionUtils.setPotion(devPotion, potion);
		list.add(devPotion);
		return list;
	}
	
	@Override //configure effects, this is applied every 10 seconds
	public void applyEffectTick(LivingEntity pl, int x)
	{
		Player player;
		if(pl instanceof Player)
		{
			player = (Player) pl;
		}else
		{
			pl.removeEffect(this);
			return;
		}
		addEffects(player);
		int xp = player.totalExperience; 
		if(xp <= 1)
		{
			player.kill();
		}else
		{
			player.giveExperiencePoints(-1);
		}
	}
}
