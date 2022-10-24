package com.subjectdeltav.spiritw.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Arrays;
import java.util.Collection;

import com.subjectdeltav.spiritw.init.BlockInit;
import com.subjectdeltav.spiritw.init.EnchantmentInit;
import com.subjectdeltav.spiritw.init.ItemInit;
import com.subjectdeltav.spiritw.init.TileEntityInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import de.maxhenkel.corpse.entities.CorpseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.enchants.SpiritBoundEnchantment;



public class InventoryHandler
	{
		private Map<String, ItemStack[]> itemsToRestore = new HashMap<String, ItemStack[]>();
		private Map<String, ItemStack[]> itemsOnRespawn = new HashMap<String, ItemStack[]>();
		
		@SubscribeEvent
		public void drops(LivingDropsEvent event) 
		{
			System.out.println("Entity has died...");
			if(event.getEntity() instanceof Player)
			{
				System.out.println("Entity is a Player, Preparing to Check for Enchanted Items...");
				Player player = (Player) event.getEntity(); //get player
				boolean saveItemsForRespawn = false; //will change to true when we detect enchanted items
				boolean saveItemsForTouchstone = false; //will change to true when a player has a lantern on death
				Collection<ItemEntity> droppedItems = event.getDrops(); //obtain the list of items the player is about to drop
				int droppedItemsQ = droppedItems.size();
				ItemStack[] ItemsToCheck = new ItemStack[droppedItemsQ];
				int itemInd = 0;
				//CorpseEntity corpse = player.level.getEntit 
				//convert the items to ItemStack from ItemEntity
				for(ItemEntity itemEnt : droppedItems) //convert the dropped ItemEntities collection to an array of ItemStack
				{
					ItemStack item = itemEnt.getItem();
					ItemsToCheck[itemInd] = item;
				}
				System.out.println("Converted Drops Collection to ItemStack Array...");
				ItemStack[] ItemsToSave = new ItemStack[droppedItemsQ]; //we'll put the items to save in here
				ItemStack[] ItemsForRespawn = new ItemStack[droppedItemsQ];
				System.out.println("Checking for Enchanted Items");
				if(ItemsToCheck.length == 0)
				{
					System.out.println("Error, no items in Drops");
				}
				for(int itemIndex = 0; itemIndex < droppedItemsQ; itemIndex++) 
				{
					//check each item in the array for the right enchantment, 
					//if it matches save it and remove it from the drop list
					ItemStack itemToCheck = ItemsToCheck[itemIndex];
					if(itemToCheck != null && itemToCheck.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) > 0)
					{
						ItemsToSave[itemIndex] = itemToCheck;
						saveItemsForTouchstone = true;
						System.out.println("Found and Saved Item of Correct Enchantment " + itemToCheck.getDisplayName());
						event.getDrops().remove(itemToCheck.getItem());
					}
					if(itemToCheck != null && itemToCheck.getItem() == ItemInit.SPLANTERN.get())
					{
						ItemsForRespawn[itemIndex] = itemToCheck;
						saveItemsForRespawn = true;
						event.getDrops().remove(itemToCheck.getItem());
					}
				}
				if(saveItemsForTouchstone)
				{
					itemsToRestore.put(player.getStringUUID(), ItemsToSave);
				}
				if(saveItemsForRespawn)
				{
					itemsOnRespawn.put(player.getStringUUID(), ItemsForRespawn);
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
				ItemStack[] itemsToReturn = itemsOnRespawn.get(player.getUUID().toString());
				for(int itemIndex = 0; itemIndex < itemsToReturn.length; itemIndex++)
				{
					player.addItem(itemsToReturn[itemIndex]);
				}
				spiritw.LOGGER.debug("Restored correct Items to player, erasing...");
				itemsOnRespawn.remove(player.getUUID().toString());
			}
		}
		
		@SubscribeEvent
		public void interact(PlayerInteractEvent event)
		{
			BlockPos blockPos = event.getPos();
			Level level = event.getLevel();
			Player player = (Player) event.getEntity();

			ItemStack[] itemstoRestore = new ItemStack[4];
			int toRestoreInd = 0;
			boolean restoreItems = false;
			Optional<TouchstoneTile> blEnt = level.getBlockEntity(blockPos, TileEntityInit.TOUCHSTONE_TILE.get());
			if(blEnt != null && itemsToRestore.containsKey(player.getStringUUID()) && event.getItemStack().getItem() == ItemInit.SPLANTERN.get())
			{
				ItemStack[] itemsFromDeath = itemsToRestore.get(player.getStringUUID());
				if(itemsFromDeath != null)
				{
					TouchstoneTile tile = (TouchstoneTile) level.getBlockEntity(blockPos);
					ItemStack[] itemsFromTile = tile.getSavedItems();
					for(int fromDeathInd = 0; fromDeathInd < itemsFromDeath.length; fromDeathInd++)
					{
						ItemStack checkItemFromDeath = itemsFromDeath[fromDeathInd];
						for( int fromTileInd = 0; fromTileInd < itemsFromTile.length; fromTileInd++)
						{
							ItemStack checkItemFromTile = itemsFromTile[fromTileInd];
							if(checkItemFromTile == checkItemFromDeath)
							{
								itemstoRestore[toRestoreInd] = checkItemFromDeath;
								toRestoreInd++;
								restoreItems = true;
							}
						}
						if(restoreItems)
						{
							for(int index = 0; index < itemstoRestore.length; index++)
							{
								ItemStack giveItem = itemstoRestore[index];
								player.addItem(giveItem);
							}
						}
					}
				}
				
			}
		}
	}
	


