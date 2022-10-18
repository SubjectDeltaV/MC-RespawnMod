package com.subjectdeltav.spiritw.tiles;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.subjectdeltav.spiritw.gui.TouchstoneMenu;
import com.subjectdeltav.spiritw.init.EnchantmentInit;
import com.subjectdeltav.spiritw.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TouchstoneTile extends BlockEntity implements MenuProvider {

	//Properties
	private LazyOptional<IItemHandler> LazyItemHandler = LazyOptional.empty();
	protected final ContainerData data;
	private int currentSavedItemsQ;
	private int maxSavedItems = 4;
	private int maxItemsRemaining;
	public UUID ownerPlayerID;
	private boolean itemInInput;
	private boolean itemInOutput;
	private boolean enchantedItem;
	private ItemStack lastEnchanted;
	
	//properties with a method
	private final ItemStackHandler itemHandler = new ItemStackHandler(10) 
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
		for(int i = 0; i < itemHandler.getSlots(); i++)
		{
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
		
		Containers.dropContents(this.level, this.worldPosition, inventory);
	}	
	public static void tick(Level l, BlockPos pos, BlockState state, TouchstoneTile ent) //logic for processing items, called every tick 
	{
		//System.out.println("Touchstone Ticked");
		if(l.isClientSide()) //check if on server side
		{
			return;
		}
		ent.CheckSlots();
		if(ent.itemInInput && ent.itemInOutput == false && ent.enchantedItem == false)
		{
			ItemStack item = CanEnchantItem(ent); //get the item in the first slot if it's enchantable
			if(item != null)
			{
				System.out.println("Enchanting requirements met, enchanting item");
				ItemStack enchantedResult = enchantInSlot(item); //enchant as applicable
				ent.itemHandler.insertItem(1, enchantedResult, false); // put enchanted result in second slot
				//ent.setCopiedItem(enchantedResult.copy()); //save the item in the noted slot
				ent.enchantedItem = true; //save the information that we have enchanted an item
				ent.lastEnchanted = enchantedResult.copy(); //setup for saving if the player keeps the enchantment
			}
		}else if(ent.enchantedItem && ent.itemInOutput == false) //if we enchanted an item and the item in the output slot is missing
		{
			System.out.println("Player has removed the enchanted item, deleting the original, and saving the item with new information");
			ent.itemHandler.extractItem(0, 1, false); //remove the item in the input slot
			ItemStack enchantedResult = ent.lastEnchanted;
			ent.setCopiedItem(enchantedResult); //save the item in the noted slot
			ent.enchantedItem = false; //reset to false so we can enchant a new item
		}else if(ent.enchantedItem && ent.itemInInput == false) //if we set an item to enchant but removed the root item
		{
			System.out.println("Player has removed the original, cancelling enchanted item, no item info has been saved!");
			ent.itemHandler.extractItem(1, 1, false); //remove the enchanted item, since the player won't be enchanting it
			ent.enchantedItem = false; //reset to false so we can enchant a new item;
			ent.lastEnchanted = ItemStack.EMPTY;
		}
		return;
	}
	
	@Nullable
	private static ItemStack CanEnchantItem(TouchstoneTile ent) //check if the item in slot 1 can be enchanted
	{
		ItemStack firstSlotItem = ent.itemHandler.getStackInSlot(0).copy(); //copy, don't set equal to!
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
			int currentSlot = this.currentSavedItemsQ + 2;
			this.itemHandler.insertItem(currentSlot, item, false);
			this.currentSavedItemsQ++;
			return;
		}
	}
	
	private void CheckSlots()
	{
		ItemStack inputSlot = this.itemHandler.getStackInSlot(0);
		ItemStack outputSlot = this.itemHandler.getStackInSlot(1);
		if(inputSlot == ItemStack.EMPTY)
		{
			itemInInput = false;
		}else
		{
			itemInInput = true;
		}
		if(outputSlot == ItemStack.EMPTY)
		{
			itemInOutput = false;
		}else
		{
			itemInOutput = true;
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
		if(this.itemInInput && this.itemInOutput)
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
