package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.enchants.SpiritBoundEnchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, spiritw.MODID);
	
	public static final RegistryObject<Enchantment> SPIRITBOUND = ENCHANTMENTS.register("spiritbound", SpiritBoundEnchantment::new);
}
