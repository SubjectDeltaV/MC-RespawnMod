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
		
		@SubscribeEvent(priority = EventPriority.HIGH)
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
					System.out.println("Added " + item.toString() + " to array");
					itemInd++;
				}
				System.out.println("Converted Drops Collection to ItemStack Array of size " + droppedItemsQ);
				ItemStack[] ItemsToSave = new ItemStack[droppedItemsQ]; //we'll put the items to save in here
				ItemStack[] ItemsForRespawn = new ItemStack[droppedItemsQ];
				System.out.println("Checking for Enchanted Items");
				if(ItemsToCheck.length == 0)
				{
					System.out.println("Error, no items in Drops");
				}
				int itemIndex = 0;
				while(itemIndex < droppedItemsQ) 
				{
					//check each item in the array for the right enchantment, 
					//if it matches save it and remove it from the drop list
					ItemStack itemToCheck = ItemsToCheck[itemIndex];
					if(itemToCheck != null)
					{
						System.out.println("Checking " + itemToCheck.toString());
						if(itemToCheck != null && itemToCheck.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) > 0)
						{
							ItemsToSave[itemIndex] = itemToCheck.copy();
							saveItemsForTouchstone = true;
							System.out.println("Found and Saved Item of Correct Enchantment " + itemToCheck.toString());
							event.getDrops().remove(itemToCheck.getItem());
						}else
						{
							System.out.println("Item does not have correct enchantment");
						}
						if(itemToCheck != null && itemToCheck.getItem() == ItemInit.SPLANTERN.get())
						{
							ItemsForRespawn[itemIndex] = itemToCheck.copy();
							saveItemsForRespawn = true;
							System.out.println("Found and Saved Spirit Lantern");
							event.getDrops().remove(itemToCheck.getItem());
						}else
						{
							System.out.println("Item is not a Spirit Lantern");
						}
					}else
					{
						System.out.println("Item is null");
					}
					itemIndex++;
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
					if(player == null)
					{
						spiritw.LOGGER.debug("Error, player is null!");
					}else if(itemsToReturn[itemIndex] == null)
					{
						spiritw.LOGGER.debug("Error, Item is null");
					}else 
					{
						player.addItem(itemsToReturn[itemIndex]);
					}
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
					spiritw.LOGGER.debug("A player has interacted with the touchstone with a lantern in hand, checking for any items to restore...");
					TouchstoneTile tile = (TouchstoneTile) level.getBlockEntity(blockPos);
					ItemStack[] itemsFromTile = tile.getSavedItems();
					if(!(itemsFromTile == null))
					{
						for(int fromDeathInd = 0; fromDeathInd < itemsFromDeath.length; fromDeathInd++)
						{
							ItemStack checkItemFromDeath = itemsFromDeath[fromDeathInd];
							if(checkItemFromDeath != null)
							{
								spiritw.LOGGER.debug("Comparing " + checkItemFromDeath.toString() + " against items in touchstone...");
								for( int fromTileInd = 0; fromTileInd < itemsFromTile.length; fromTileInd++)
								{
									ItemStack checkItemFromTile = itemsFromTile[fromTileInd];
									if(checkItemFromTile.equals(checkItemFromDeath))
									{
										spiritw.LOGGER.debug("Item Matches, adding it to list to restore items.");
										itemstoRestore[toRestoreInd] = checkItemFromDeath;
										toRestoreInd++;
										restoreItems = true;
									}
								}
							} else
							{
								spiritw.LOGGER.error("Item to check against touuchstone is null, ignoring...");
							}
							
						}
						if(restoreItems)
						{
							for(int index = 0; index < itemstoRestore.length; index++)
							{
								spiritw.LOGGER.debug("Giving Items to Player...");
								ItemStack giveItem = itemstoRestore[index];
								player.addItem(giveItem);
							}
						}
					} else
					{
						spiritw.LOGGER.error("items from Touchstone are null, unable to copy");
					}
					
				}
				
			}
		}
	}
	


