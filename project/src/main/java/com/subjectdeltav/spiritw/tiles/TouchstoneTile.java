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
				ItemStack enchantedResult = enchantInSlot(item); //enchant as applicable
				ent.itemHandler.setStackInSlot(2, enchantedResult); // put enchanted result in second slot
				ent.setCopiedItem(enchantedResult); //save the item in the noted slot
				ent.enchantedItem = true; //save the information that we have enchanted an item
			}
		}else if(ent.enchantedItem && ent.itemInInput && ent.itemInOutput == false)
		{
			ent.itemHandler.extractItem(0, 1, false); //clear out the first slot if the player takes the enchanted item
			ent.enchantedItem = false;
		}else if(ent.enchantedItem && ent.itemInInput == false && ent.itemInOutput)
		{
			ent.itemHandler.setStackInSlot(2, ItemStack.EMPTY); //clear out the second slot if the player removes the item to enchant
			ent.enchantedItem = false;
		}else if(ent.enchantedItem && ent.itemInInput == false && ent.itemInOutput == false)
		{
			ent.enchantedItem = false; //for any edge cases
		}
		return;
	}
	
	@Nullable
	private static ItemStack CanEnchantItem(TouchstoneTile ent) //check if the item in slot 1 can be enchanted
	{
		ItemStack firstSlotItem = ent.itemHandler.getStackInSlot(1);
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
		if(item.isEnchantable())
		{
			if(!item.isEnchanted() || item.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) == 0) //check if item has enchantment already
			{
				item.enchant(EnchantmentInit.SPIRITBOUND.get(), 1); //apply level 1 of enchantment
				return item;
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
			this.itemHandler.setStackInSlot(currentSlot, item);
			return;
		}
	}
	
	private void CheckSlots()
	{
		ItemStack inputSlot = this.itemHandler.getStackInSlot(1);
		ItemStack outputSlot = this.itemHandler.getStackInSlot(2);
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
