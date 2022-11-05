package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.SpiritwMobEffect;
import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.Cureall;
import com.subjectdeltav.spiritw.effects.EnterGhostState;
import com.subjectdeltav.spiritw.effects.Ghost;
import com.subjectdeltav.spiritw.effects.ResurrectionSickness;
import com.subjectdeltav.spiritw.effects.Revival_Sickness;
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
	public static final RegistryObject<MobEffect> REVIVAL_SICKNESS = EFFECTS.register("revival_sickness", Revival_Sickness::new);
	public static final RegistryObject<MobEffect> CUREALL = EFFECTS.register("cure_all", Cureall::new);
	public static final RegistryObject<MobEffect> GHOST = EFFECTS.register("ghost", Ghost::new);
	public static final RegistryObject<MobEffect> RESURRECTION_SICKNESS = EFFECTS.register("resurrection_sickness", ResurrectionSickness::new);
	public static final RegistryObject<MobEffect> ENTER_GHOST_STATE = EFFECTS.register("enter_ghost_state", EnterGhostState::new);
}
