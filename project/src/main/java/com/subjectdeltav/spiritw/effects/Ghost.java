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
import net.minecraft.world.phys.Vec3;

/**
 * MobEffect APplied to Player to achieve spiritwalking
 * Players are immune to ordinary damage
 * Players are invisible
 * Visibility is limtied by adding the Blind MobEffect
 * XP is drained at a set rate. If it reaches zero the player is killed and
 * must respawn normally
 * @author Mount
 *
 */
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
	/**
	 * Sets the internally store Spirit Lantern
	 * @param lant the SpLantern to set
	 */
	public void setLantern(SpLantern lant)
	{
		this.lantern = lant;
	}
	
	/**
	 * How Many ticks are left before player death
	 * @param playerXP XP of the player used to estimate how long until ghost expiration
	 * @return int of ticks before ghost expiration
	 */
	public int estimateTicks(int playerXP) //estimate how many ticks the remaining XP has
	{
		this.currentXP = playerXP;
		this.ticksRemaining = playerXP * 6;
		spiritw.LOGGER.debug(ticksRemaining + " ticks left in ghost effect");
		return ticksRemaining;
	}
	
	/**
	 * Adds the specific effects for ghosts
	 * Includes Invisibility, Blindness and Invulnerability from damage
	 * @param player
	 */
	private void addEffects(Player player)
	{
		player.addEffect(new MobEffectInstance(blind, effectLength * 20, 3));
		if(!player.isInvisible())
		{
			player.setInvisible(true);
		}
		if(!player.isInvulnerable())
		{
			player.setInvulnerable(true);
		}
	}
	
	
	//Overrode Methods
	/**
	 * Configure effect to happen every 10 seconds
	 */
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
	
	/**
	 * Curing Items by default are just the CureAllPotion
	 * Milk does NOT work, and should not work
	 */
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
	
	/**
	 * Based on isDurationTickEffect(), applies effects every 10 seconds
	 * Also drains xp at set rate
	 */
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
