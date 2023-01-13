package com.subjectdeltav.spiritw.tiles;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.gui.TouchstoneMenu;
import com.subjectdeltav.spiritw.init.EnchantmentInit;
import com.subjectdeltav.spiritw.init.PotionInit;
import com.subjectdeltav.spiritw.init.TileEntityInit;
import com.subjectdeltav.spiritw.item.SpLantern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import de.maxhenkel.corpse.corelib.death.*;

public class TouchstoneTile extends BlockEntity implements MenuProvider {

	//Properties
	private LazyOptional<IItemHandler> LazyItemHandler = LazyOptional.empty();
	protected ContainerData data;
	protected int currentSavedItemsQ;
	int maxSavedItems = 3;
	int maxItemsRemaining;
	protected UUID ownerPlayerID;
	protected Player player;
	public boolean playerIsSet = false;
	private boolean[] itemInInput;
	private boolean[] itemInOutput;
	protected boolean enchantedItem;
	private ItemStack lastEnchanted;
	private static int levelsToEnchant = 5;
	private static int levelsToBrew = 3;
	private int activatedPotion; //0 is no, 1 is throwable, 2 is drinkable
	//private int[] savedItemIDs;
	
	//set slots for easy adjusting
	private static int enchantInputSlot = 0;
	private static int enchantOutputSlot = 1;
	private static int brewInputSlot1 = 2;
	private static int brewInputSlot2 = 3;
	private static int brewOutputSlot = 4;
	private static int savedItem1 = 5;
	private static int savedItem2 = 6;
	private static int savedItem3 = 7;
	
	//properties with an override
	private final ItemStackHandler itemHandler = new ItemStackHandler(8) 
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			setChanged();
		}
	};
	
	//CONSTRUCTOR
	public TouchstoneTile(BlockPos pos, BlockState state) 
	{
		super(TileEntityInit.TOUCHSTONE_TILE.get(), pos, state);
		this.enchantedItem = false;
		this.currentSavedItemsQ = 0;
		itemInInput = new boolean[3];
		itemInOutput = new boolean[2];
		this.activatedPotion = 0;
		this.data = new ContainerData()
				{
					@Override
					public int get(int index)
					{
						return switch (index)
								{
									case 0 -> TouchstoneTile.this.currentSavedItemsQ;
									case 1 -> TouchstoneTile.this.maxSavedItems;
									case 2 -> TouchstoneTile.this.maxItemsRemaining;
									default -> 0;
								};
					}
					
					@Override
					public void set(int i, int v)
					{
						switch(i)
						{
							case 0 -> TouchstoneTile.this.currentSavedItemsQ = v;
							case 1 -> TouchstoneTile.this.maxSavedItems = v;
							case 2 -> TouchstoneTile.this.maxItemsRemaining = v;
						}
					}
					
					@Override
					public int getCount()
					{
						return 3;
					}
				};
	}
	
	//custom methods
	public void drops()
	{
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for(int i = 6; i < itemHandler.getSlots(); i++) //only get items starting in slot 6
		{
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
		inventory.addItem(itemHandler.getStackInSlot(0)); //add item in input slot
		Containers.dropContents(this.level, this.worldPosition, inventory);
	}	
	
	public static void tick(Level l, BlockPos pos, BlockState state, TouchstoneTile ent) //logic for processing items, called every tick 
	{
		//System.out.println("Touchstone Ticked");
		Item brewedPotion = null; //hopefully this won't cause a crash
		if(l.isClientSide()) //check if on server side
		{
			return;
		}
		ent.CheckSlots();
		if(ent.itemInInput[0] && ent.itemInOutput[0] == false && ent.enchantedItem == false)
		{
			ItemStack item = CanEnchantItem(ent); //get the item in the first slot if it's enchantable
			if(item != null && ent.player.totalExperience > levelsToEnchant)
			{
				System.out.println("Enchanting requirements met, enchanting item");
				ItemStack enchantedResult = enchantInSlot(item); //enchant as applicable
				ent.itemHandler.insertItem(enchantOutputSlot, enchantedResult, false); // put enchanted result in second slot
				//ent.setCopiedItem(enchantedResult.copy()); //save the item in the noted slot
				ent.enchantedItem = true; //save the information that we have enchanted an item
				ent.lastEnchanted = enchantedResult.copy(); //setup for saving if the player keeps the enchantment
			}
		}else if(ent.enchantedItem && ent.itemInOutput[0] == false) //if we enchanted an item and the item in the output slot is missing
		{
			System.out.println("Player has removed the enchanted item, deleting the original, and saving the item with new information");
			if(ent.player.experienceLevel > levelsToEnchant)
			{
				try
				{
					ent.player.experienceLevel = ent.player.experienceLevel - levelsToEnchant;
				}catch(Exception e)
				{
					spiritw.LOGGER.debug("An error occurred trying to subtract levels for enchant!");
				}
				ent.itemHandler.extractItem(enchantInputSlot, 1, false); //remove the item in the input slot
				ItemStack enchantedResult = ent.lastEnchanted;
				ent.setCopiedItem(enchantedResult); //save the item in the noted slot
				ent.enchantedItem = false; //reset to false so we can enchant a new item
				spiritw.LOGGER.debug("removed original, and decucted xp levels");
			}
		}else if(ent.enchantedItem && ent.itemInInput[0] == false) //if we set an item to enchant but removed the root item
		{
			System.out.println("Player has removed the original, cancelling enchanted item, no item info has been saved!");
			ent.itemHandler.extractItem(enchantOutputSlot, 1, false); //remove the enchanted item, since the player won't be enchanting it
			ent.enchantedItem = false; //reset to false so we can enchant a new item;
			ent.lastEnchanted = ItemStack.EMPTY;
		}
		if(ent.itemInInput[1] && ent.itemInInput[2] && ent.itemInOutput[1] == false) //check for new items, and see if we can output a valid potion
		{
			spiritw.LOGGER.debug("Items detected for brewing in brewing slots...");
			if(ent.player.experienceLevel > 3)
			{
				spiritw.LOGGER.debug("Player has enough XP to activate the potion, brewing...");
				ItemStack inputBottle = ent.itemHandler.getStackInSlot(brewInputSlot1);
				ItemStack material = ent.itemHandler.getStackInSlot(brewInputSlot2);
				ItemStack potion = ent.checkAndReturnBrewables(inputBottle, material);
				try 
				{
					ent.itemHandler.insertItem(brewOutputSlot, potion, false);
					brewedPotion = potion.getItem();
					ent.activatedPotion = 1;
				}catch(Exception e)
				{
					spiritw.LOGGER.error("An error occured while the touchstone was brewing. Printing stacktrace...");
					e.printStackTrace();
				}

			}else
			{
				spiritw.LOGGER.debug("Player has insufficent experience levels, unable to brew potion");
			}
		}else if(ent.activatedPotion > 0 && ent.itemInOutput[1] == false) //if player removes potion, deduct the levels, if they no longer have the levels, destroy the potion
		{
			spiritw.LOGGER.debug("Player has removed potion, deducting experience levels");
			if(ent.activatedPotion == 1 && ent.player.experienceLevel > levelsToBrew)
			{
				ent.itemHandler.extractItem(brewInputSlot2, 1, false);
				ent.itemHandler.extractItem(brewInputSlot2, 1, false);
				ent.activatedPotion = 0;
				try 
				{
					ent.player.experienceLevel = ent.player.experienceLevel - levelsToEnchant;					
				}catch(Exception e)
				{
					spiritw.LOGGER.error("An error occurred deducting levels");
					e.printStackTrace();
				}
			}else if(ent.activatedPotion == 2 && ent.player.experienceLevel > 5)
			{
				ent.itemHandler.extractItem(9, 1, false);
				ent.activatedPotion = 0;
				try 
				{
					ent.player.experienceLevel = ent.player.experienceLevel - levelsToEnchant;					
				}catch(Exception e)
				{
					spiritw.LOGGER.error("An error occurred deducting levels");
					e.printStackTrace();
				}
			}else
			{
				spiritw.LOGGER.debug("Error: Either  the touchstone block is in an incorrect state or the player no longer has enough levels to brew");
				ent.itemHandler.insertItem(brewOutputSlot, new ItemStack(brewedPotion), false);
				ent.itemHandler.extractItem(brewOutputSlot, 1, false); //put item back into the touchstone and delete it
			}
		}else if(ent.activatedPotion == 1)
		{
			if(ent.itemInInput[1] == false || ent.itemInInput[2] == false)
			{
				spiritw.LOGGER.debug("Items removed from brewing input slot(s), cancelling brewing");
				ent.itemHandler.extractItem(brewOutputSlot, 1, false); //remove brewed potion
				ent.activatedPotion = 0; //reset to not brewed
			}
		}else if(ent.activatedPotion == 2 && ent.itemInInput[1] == false)
		{
			spiritw.LOGGER.debug("Items removed from brewing input slot(s), cancelling brewing");
			ent.itemHandler.extractItem(brewOutputSlot, 1, false); //remove brewed potion
			ent.activatedPotion = 0; //reset to not brewed
		}
	}
	
	@Nullable
	private static ItemStack CanEnchantItem(TouchstoneTile ent) //check if the item in slot 1 can be enchanted
	{
		ItemStack firstSlotItem = ent.itemHandler.getStackInSlot(enchantInputSlot).copy(); //copy, don't set equal to!
		boolean hasEnchantableInFirstSlot = firstSlotItem.isEnchantable();
		if(hasEnchantableInFirstSlot && ent.currentSavedItemsQ >= ent.maxSavedItems) //also check if we have slots available
		{
			return null;
		}else if(hasEnchantableInFirstSlot)
		{
			return firstSlotItem;
		}else
		{
			return null;
		}
	}

	@Nullable
	private static ItemStack enchantInSlot(ItemStack item) //enchant item through method
	{
		ItemStack outputItem = item.copy();
		if(item.isEnchantable())
		{
			if(!item.isEnchanted() || item.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) == 0) //check if item has enchantment already
			{
				outputItem.enchant(EnchantmentInit.SPIRITBOUND.get(), 1); //apply level 1 of enchantment
				return outputItem;
			}else
			{
				return item; 
			}
		}else
		{
			return item;
		}
	}
	
	private void setCopiedItem(ItemStack item)
	{
		if(this.currentSavedItemsQ >= this.maxSavedItems) //make sure we have slots available
		{
			return;
		}else
		{
			int currentSlot = this.currentSavedItemsQ + 5;
			this.itemHandler.insertItem(currentSlot, item, false);
			this.currentSavedItemsQ++;
			return;
		}
	}
	
	private void CheckSlots()
	{
		ItemStack inputSlot = this.itemHandler.getStackInSlot(enchantInputSlot);
		ItemStack outputSlot = this.itemHandler.getStackInSlot(enchantOutputSlot);
		ItemStack potionInput = this.itemHandler.getStackInSlot(brewInputSlot1);
		ItemStack potionAdditive = this.itemHandler.getStackInSlot(brewInputSlot2);
		ItemStack potionOutput = this.itemHandler.getStackInSlot(brewOutputSlot);
		if(inputSlot == ItemStack.EMPTY)
		{
			itemInInput[0] = false;
		}else
		{
			itemInInput[0] = true;
		}
		if(potionInput == ItemStack.EMPTY)
		{
			itemInInput[1] = false;
		}else
		{
			itemInInput[0] = true;
		}
		if(potionAdditive == ItemStack.EMPTY)
		{
			itemInInput[2] = false;
		}else
		{
			itemInInput[2] = true;
		}
		if(outputSlot == ItemStack.EMPTY)
		{
			itemInOutput[0] = false;
		}else
		{
			itemInOutput[0] = true;
		}
		if(potionOutput.equals(ItemStack.EMPTY))
		{
			itemInOutput[1] = false;
		}else
		{
			itemInOutput[1] = false;
		}
	}
	

	private ItemStack checkAndReturnBrewables(ItemStack bottle, ItemStack sand)
	{
		ItemStack output = null;
		if(bottle.equals(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER))
				&& sand.equals(new ItemStack(Items.SOUL_SAND)))
		{
			output = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), PotionInit.REVIVE_POTION.get());
		}else if(bottle.equals(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionInit.INACTIVE_REVIVE.get())))
		{
			output = PotionUtils.setPotion(new ItemStack(Items.POTION), PotionInit.REVIVE_POTION.get());
		}
		return output;
	}

	public void setPlayer(Player player)
	{
		this.ownerPlayerID = player.getUUID();
		this.player = player;
		playerIsSet = true;
	}
	
	public UUID getPlayerID()
	{
		return ownerPlayerID;
	}
	
	public boolean scanForAndReturnItems(Player player, SpLantern lantern) throws Exception
	{
		//import arrays and configure variables
		ItemStack[] itemsFromBlock = this.getSavedItems();
		ItemStack[] itemsFromLantern = null;
		boolean didTransferItems = false;
		if(lantern.getSavedStatus())
		{
			try {
				itemsFromLantern = lantern.getItemsToReturn();
			} catch (Exception e) {
				spiritw.LOGGER.error("Error trying to get items from Lantern");
				e.printStackTrace();
			}
		}else
		{
			spiritw.LOGGER.debug("Lantern has no saved items, unable to return anything");
			return false;
		}
		
		//scan for items
		try 
		{
			for(ItemStack item : itemsFromLantern)
			{
				int enchantLvl = 0;
				if(item != null)
				{
					try
					{
						enchantLvl = item.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get());
					}catch(Exception e)
					{
						spiritw.LOGGER.error("Error occurred when trying to get Bound Items Enchantment Level");
					}
					if(enchantLvl > 0)
					{
						for(ItemStack itemCompare : itemsFromBlock)
						{
							if(item.is(itemCompare.getItem()))
							{
								player.addItem(item);
								spiritw.LOGGER.debug("Returned Item" + item.toString() + "to player");
							}
						}
					}
				}
			}
			didTransferItems = true;
		}catch(Exception e)
		{
			spiritw.LOGGER.error("Unkown Error occured comparing items from death to saved items in Touchstone");
			throw(e);
		}
		
		//clear out lantern if applicable
		if(didTransferItems)
		{
			boolean clearedlantern = lantern.clearSavedItems();
			if(clearedlantern)
			{
				spiritw.LOGGER.debug("Cleared the lantern of its saved items");
			}else
			{
				throw new Exception("Error Clearing Items from the Lantern");
			}
		}
		
		return true;
	}
	
	@Nullable
	public ItemStack[] getSavedItems() //scans all items currently in the saved items slots and returns an array of items for all saved items
	{
		ItemStack slot1 = itemHandler.getStackInSlot(2);
		ItemStack slot2 = itemHandler.getStackInSlot(3); 
		ItemStack slot3 = itemHandler.getStackInSlot(4);
		ItemStack slot4 = itemHandler.getStackInSlot(5); 
		int outputSize = 0;
		if(slot1.isEmpty() == false)
		{
			outputSize++;
			if(slot2.isEmpty() == false)
			{
				outputSize++;
				if(slot3.isEmpty() == false)
				{
					outputSize++;
					if(slot3.isEmpty() == false)
					{
						outputSize++;
					}
				}
			}
			System.out.println("There are " + outputSize + " items available from touchstone of owner " + this.ownerPlayerID);
		}else
		{
			System.out.println("no items saved, ignoring request for array of saved items");
			return null;
		}
		ItemStack[] outputStack = new ItemStack[outputSize];
		outputStack[0] = slot1;
		if(outputSize > 1)
		{
			outputStack[1] = slot2;
			if(outputSize > 2)
			{
				outputStack[2] = slot3;
				if(outputSize > 3)
				{
					outputStack[3] = slot4;
				}
			}
		}
		return outputStack;
	}
	
	public void restoreItems(ItemStack item, int slotN)
	{
		if(item != null)
		{
			this.itemHandler.insertItem(slotN, item, false);
		}
	}

	
	//overrode methods
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player)
	{
		return new TouchstoneMenu(id, inventory, this, this.data);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Touchstone");
	}
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == ForgeCapabilities.ITEM_HANDLER)
		{
			return LazyItemHandler.cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onLoad() 
	{
		super.onLoad();
		LazyItemHandler = LazyOptional.of(() -> itemHandler);
		this.CheckSlots();
		if(this.itemInInput[0] && this.itemInOutput[0])
		{
			this.enchantedItem = true;
		}
	}
	
	@Override
	public void invalidateCaps()
	{
		super.invalidateCaps();
		LazyItemHandler.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt)
	{
		nbt.put("inventory", itemHandler.serializeNBT());
		super.saveAdditional(nbt);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
	}	
}
