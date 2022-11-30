package com.subjectdeltav.spiritw.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;

import com.subjectdeltav.spiritw.init.EnchantmentInit;
import com.subjectdeltav.spiritw.init.ItemInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.PlayerDeathEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.ModEffects;



public class InventoryHandler
	{
		//this handler ensures the lantern stays with the player after respawn and also enables the spiritbound enchantment works in certain case
	
		private Map<String, ItemStack[]> itemsToRestore = new HashMap<String, ItemStack[]>();
		private Map<String, ItemStack[]> itemsOnRespawn = new HashMap<String, ItemStack[]>();
		//private Map<String, ItemStack[]> itemsRemoveFromCorpse = new HashMap<String, ItemStack[]>(); //removed, tracking items with this was never implemented
		
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void drops(PlayerDeathEvent event) 
		{
			System.out.println("Entity has died...");
			if(event.getPlayer() instanceof Player)
			{
				System.out.println("Entity is a Player, Preparing to Check for Enchanted Items...");
				Player player = (Player) event.getPlayer(); //get player
				boolean saveItemsForRespawn = false; //will change to true when we detect enchanted items
				boolean saveItemsForTouchstone = false; //will change to true when a player has a lantern on death
				Death death = event.getDeath();
				Collection<ItemStack> droppedItems = death.getAllItems(); //obtain the list of items the player is about to drop
				int droppedItemsQ = droppedItems.size();
				ItemStack[] ItemsToCheck = new ItemStack[droppedItemsQ];
				int itemInd = 0;
				//CorpseEntity corpse = player.level.getEntit 
				//convert the items to ItemStack from ItemEntity
				for(ItemStack itemSt : droppedItems) //convert the dropped ItemStack collection to an array
				{
					ItemStack item = itemSt;
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
							ItemsToSave[itemIndex] = itemToCheck;
							saveItemsForTouchstone = true;
							System.out.println("Found and Saved Item of Correct Enchantment " + itemToCheck.toString());
							droppedItems.remove(itemToCheck);
						}else
						{
							System.out.println("Item does not have correct enchantment");
						}
						if(itemToCheck != null && itemToCheck.getItem() == ItemInit.SPLANTERN.get())
						{
							ItemsForRespawn[itemIndex] = itemToCheck;
							saveItemsForRespawn = true;
							System.out.println("Found and Saved Spirit Lantern");
							droppedItems.remove(itemToCheck);
							
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
				//death.processDrops(setDrops);
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
			if (event.isWasDeath() && itemsOnRespawn.containsKey(player.getUUID().toString())) 
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
		
		@SubscribeEvent(priority = EventPriority.LOW)
		public void interact(PlayerInteractEvent event)
		{
			BlockPos blockPos = event.getPos();
			Level level = event.getLevel();
			Player player = (Player) event.getEntity();

			ItemStack[] itemstoRestore = new ItemStack[4];
			int toRestoreInd = 0;
			boolean restoreItems = false;
			BlockEntity ent = level.getBlockEntity(blockPos);
			if(ent instanceof TouchstoneTile) //prevent CTD from unable to cast
			{
				TouchstoneTile tile = (TouchstoneTile) level.getBlockEntity(blockPos);
				if(tile != null && event.getItemStack().getItem().equals(ItemInit.SPLANTERN.get()) && player.hasEffect(ModEffects.GHOST))
				{
					player.removeAllEffects(); //this will cancel the ghost effect
					player.setHealth(Player.MAX_HEALTH);
					player.setInvisible(false);
					player.setInvulnerable(false);
					player.addEffect(new MobEffectInstance(ModEffects.RESSURECTION_SICKNESS, 3600));
				}
				if(tile != null && itemsToRestore.containsKey(player.getStringUUID()) && event.getItemStack().getItem().equals(ItemInit.SPLANTERN.get()))
				{
					ItemStack[] itemsFromDeath = itemsToRestore.get(player.getStringUUID());
					if(itemsFromDeath != null)
					{
						spiritw.LOGGER.debug("A player has interacted with the touchstone with a lantern in hand, checking for any items to restore...");

						ItemStack[] itemsFromTile = tile.getSavedItems();
						if(!(itemsFromTile == null))
						{
							//ItemStack[] removeItemsFromCorpse = new ItemStack[itemsFromTile.length];
							for(int fromDeathInd = 0; fromDeathInd < itemsFromDeath.length; fromDeathInd++)
							{
								ItemStack checkItemFromDeath = itemsFromDeath[fromDeathInd];
								if(checkItemFromDeath != null)
								{
									spiritw.LOGGER.debug("Comparing " + checkItemFromDeath.toString() + " against items in touchstone...");
									for( int fromTileInd = 0; fromTileInd < itemsFromTile.length; fromTileInd++)
									{
										ItemStack checkItemFromTile = itemsFromTile[fromTileInd];
										if(checkItemFromDeath.is(checkItemFromTile.getItem()))
										{
											spiritw.LOGGER.debug("Item Matches, adding it to list to restore items.");
											if(toRestoreInd < itemstoRestore.length)
											{
												itemstoRestore[toRestoreInd] = checkItemFromDeath;
												toRestoreInd++;
												restoreItems = true;
											}else
											{
												spiritw.LOGGER.error("Maximum number of items to restore has been exceeded, not restoring further items");
											}
										}
									}
								} else
								
									spiritw.LOGGER.error("Item to check against touchstone is null, ignoring...");
								}
							}
							if(restoreItems)
							{
								for(int index = 0; index < itemstoRestore.length; index++)
								{
									spiritw.LOGGER.debug("Giving Items to Player...");
									ItemStack giveItem = itemstoRestore[index];
									if(giveItem != null)
									{
										player.addItem(giveItem);
									}
								}
								//itemsRemoveFromCorpse.put(player.getStringUUID(), itemsToRestore.get(player.getStringUUID()).clone());
								itemsToRestore.remove(player.getStringUUID());

							}
						} else
						{
							spiritw.LOGGER.error("items from Touchstone are null, unable to copy");
						}
						
				}
			}
			
				
		}
	}
	


