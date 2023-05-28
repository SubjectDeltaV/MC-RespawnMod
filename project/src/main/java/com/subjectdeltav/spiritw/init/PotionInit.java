package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.spiritw;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Initializer for our Potions
 * @author Mount
 *
 */
public class PotionInit 
{
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, spiritw.MODID);
	
	public static final RegistryObject<Potion> REVIVE_POTION = POTIONS.register("revive_potion", () -> new Potion(new MobEffectInstance(EffectInit.REVIVE.get())));
	public static final RegistryObject<Potion> INACTIVE_REVIVE = POTIONS.register("inactive_revive_potion", () -> new Potion());
	public static final RegistryObject<Potion> CUREALL_POTION = POTIONS.register("cureall_potion", () -> new Potion(new MobEffectInstance(EffectInit.CUREALL.get())));
}
