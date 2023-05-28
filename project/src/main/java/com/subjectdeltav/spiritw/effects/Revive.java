package com.subjectdeltav.spiritw.effects;

import com.subjectdeltav.spiritw.init.EffectInit;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * Unused
 * @author Mount
 *
 */
public class Revive extends InstantenousMobEffect
{
	//This effect is to cure the player from the wounded effect
	
	
	public Revive() 
	{
		super(MobEffectCategory.BENEFICIAL, 2039587);
	}
	
	@Override
	public void applyEffectTick(LivingEntity mob, int x)
	{
		int amplifier = 1;
		//remove wounded status, if the target has it
		if(mob.hasEffect(EffectInit.WOUNDED.get()))
		{
			MobEffectInstance wound = mob.getEffect(EffectInit.WOUNDED.get());
			amplifier = wound.getAmplifier();
			//ItemStack cure = ItemStack.EMPTY;
			//cure = PotionUtils.setPotion(cure, PotionInit.REVIVE_POTION.get());
			mob.removeEffect(EffectInit.WOUNDED.get());
			mob.removeEffect(MobEffects.HUNGER);
			mob.removeEffect(MobEffects.DARKNESS);
			mob.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
			Player player = (Player) mob;
			player.setForcedPose(null); //clear dying pose
			//player.addEffect(new MobEffectInstance(EffectInit.REVIVAL_SICKNESS.get(), 2400, 1));
		}
		//apply revival sickness, players will get revival sickness regardless of whether or not they were down
		if(mob.hasEffect(EffectInit.REVIVAL_SICKNESS.get()) == false); //only apply if the effect is not already there
		{
			MobEffectInstance sickness = new MobEffectInstance(EffectInit.REVIVAL_SICKNESS.get(), 2400, amplifier);
			mob.addEffect(sickness);
		}
	}

}
