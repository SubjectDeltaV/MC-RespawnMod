package com.subjectdeltav.spiritw.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * The Enchantment that allows you to keep your items when reviving from a ghost
 * at the touchstone
 * The enchantment itself has no special properties
 * Logic is handled separately through events
 * @author Mount
 *
 */
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


	
	