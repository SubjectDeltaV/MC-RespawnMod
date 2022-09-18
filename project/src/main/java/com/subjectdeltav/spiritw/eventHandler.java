package com.subjectdeltav.spiritw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Arrays;
import java.util.Collection;

import com.subjectdeltav.spiritw.init.EnchantmentInit;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import com.subjectdeltav.spiritw.enchants.SpiritBoundEnchantment;



public class eventHandler
	{
		private Map<String, ItemStack[]> itemsToRestore = new HashMap<String, ItemStack[]>();
		
		@SubscribeEvent
		public void drops(LivingDropsEvent event) 
		{
			System.out.println("Entity has died...");
			if(event.getEntity() instanceof Player)
			{
				System.out.println("Entity is a Player, Preparing to Check for Enchanted Items...");
				Player player = (Player) event.getEntity(); //get player
				boolean saveItemsForRespawn = false; //will change to true when we detect enchanted items
				Collection<ItemEntity> droppedItems = event.getDrops(); //obtain the list of items the player is about to drop
				int droppedItemsQ = droppedItems.size();
				ItemStack[] ItemsToCheck = new ItemStack[droppedItemsQ];
				int itemInd = 0;
				//convert the items to ItemStack from ItemEntity
				for(ItemEntity itemEnt : droppedItems) //convert the dropped ItemEntities collection to an array of ItemStack
				{
					ItemStack item = itemEnt.getItem();
					ItemsToCheck[itemInd] = item;
				}
				ItemStack[] ItemsToSave = new ItemStack[droppedItemsQ]; //we'll put the items to save in here
				for(int itemIndex = 0; itemIndex < droppedItemsQ; itemIndex++) 
				{
					//check each item in the array for the right enchantment, 
					//if it matches save it and remove it from the drop list
					ItemStack itemToCheck = ItemsToCheck[itemIndex];
					if(itemToCheck != null && itemToCheck.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) > 0)
					{
						ItemsToSave[itemIndex] = itemToCheck;
						saveItemsForRespawn = true;
						System.out.println("Found  and Saved Item of Correct Enchantment " + itemToCheck.getDisplayName());
						event.getDrops().remove(itemToCheck);
					}
				}
				if(saveItemsForRespawn)
				{
					itemsToRestore.put(player.getStringUUID(), ItemsToSave);
				}
			}
			else
			{
				System.out.println("Entity is not player, ignoring...");
			}
		}
		
		@SubscribeEvent
		public void respawn(PlayerEvent.Clone event) 
		{
			Player player = (Player) event.getEntity();
			if (event.isWasDeath() && itemsToRestore.containsKey(player.getUUID().toString())) 
			{
				spiritw.LOGGER.debug("Detected Items for Restoring to Player for Respawn with correct Enchantment Type 'Spiritbound'");
				ItemStack[] itemsToReturn = itemsToRestore.get(player.getUUID().toString());
				for(int itemIndex = 0; itemIndex < itemsToReturn.length; itemIndex++)
				{
					player.addItem(itemsToReturn[itemIndex]);
				}
				spiritw.LOGGER.debug("Restored correct Items to player, erasing...");
				itemsToRestore.remove(player.getUUID().toString());
			}
		}
		
	}
	


