package com.subjectdeltav.spiritw.enchants;

import java.lang.System.Logger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.init.EnchantmentInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class SpiritBoundEnchantment extends Enchantment 
{
	public SpiritBoundEnchantment() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	UUID ownerID;
	TouchstoneTile touchstone;
	
	
	
	@Override
	public int getMaxLevel() 
	{
		return 1;
	}
}


	
	