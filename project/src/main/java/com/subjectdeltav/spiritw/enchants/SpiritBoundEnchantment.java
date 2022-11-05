package com.subjectdeltav.spiritw.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SpiritBoundEnchantment extends Enchantment 
{
	public SpiritBoundEnchantment() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	
	@Override
	public int getMaxLevel() 
	{
		return 2;
	}
}


	
	