package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.SpiritwMobEffect;
import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.Revive;
import com.subjectdeltav.spiritw.effects.wounded;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectInit 
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, spiritw.MODID);
	
	public static final RegistryObject<MobEffect> WOUNDED = EFFECTS.register("wounded", wounded::new);
	
	public static final RegistryObject<MobEffect> REVIVE = EFFECTS.register("revive", Revive::new);
}
