package com.subjectdeltav.spiritw;

import java.util.HashMap;
import java.util.Map;

import com.subjectdeltav.spiritw.init.EnchantmentInit;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.subjectdeltav.spiritw.enchants.SpiritBoundEnchantment;


public class Events {
	private Map<String, ItemStack[]> itemsToRestore = new HashMap<String, ItemStack[]>();
	
	@SubscribeEvent
	public void downed(LivingDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			boolean returnItems = false;
			Inventory inven = player.getInventory();
			int invenCount = inven.getContainerSize();
			ItemStack[] itemsToReturn = new ItemStack[invenCount];
			int itemsToReturnQ = 0;
			for(int invIndex = 0; invIndex < invenCount; invIndex++)
			{
				ItemStack item = inven.getItem(invIndex);
				if(item.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) > 0)
					{
						itemsToReturn[invIndex] = item;
						returnItems = true;
					}
				else
				{
					returnItems = false;
				}
			}
			if(returnItems)
			{
				itemsToRestore.put(player.getStringUUID(), itemsToReturn);
			}
		}
	}
	@SubscribeEvent
	public void respawn(PlayerEvent.Clone event) {
		Player player = event.getEntity();
		if (event.isWasDeath() && itemsToRestore.containsKey(player.getUUID().toString())) {
			ItemStack[] itemsToReturn = itemsToRestore.get(player.getUUID().toString());
			Inventory inven = player.getInventory();
			int returnSize = itemsToReturn.length;
			for(int returnIndex = 0; returnIndex < returnSize; returnIndex++)
			{
				inven.add(itemsToReturn[returnIndex]);
			}
			itemsToRestore.remove(player.getUUID().toString());

		}
	}
}
