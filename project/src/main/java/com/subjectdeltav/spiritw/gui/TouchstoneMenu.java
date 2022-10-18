package com.subjectdeltav.spiritw.gui;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.subjectdeltav.spiritw.init.BlockInit;
import com.subjectdeltav.spiritw.init.MenuTypesInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TouchstoneMenu extends AbstractContainerMenu {

	//properties
	public final TouchstoneTile tileEntity;
	private final Level world;
	public final ContainerData data;
	
	//Constructors
	public TouchstoneMenu(int id, Inventory inv, FriendlyByteBuf extraData)
	{
		this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
	}
	
	public TouchstoneMenu(int id, Inventory inv, BlockEntity ent, ContainerData data)
	{
		super(MenuTypesInit.TOUCHSTONE_MENU.get(), id);
		checkContainerSize(inv, 10);
		this.world = inv.player.level;
		this.data = data;
		addPlayerInventory(inv);
		addPlayerHotbar(inv);
		tileEntity = (TouchstoneTile) ent;
		this.tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
			SlotItemHandler outputItemSlot = new SlotItemHandler(handler, 1, 63, 30)
					{
						@Override
						public boolean mayPlace(@NotNull ItemStack item)
						{
							return false; //ensure players cannot place anything in this slot
						}
					};
			SlotItemHandler itemSave1 = new SlotItemHandler(handler, 2, 94, 30)
					{
						@Override
						public boolean mayPlace(@NotNull ItemStack item)
						{
							return false;
						}
						@Override
						public boolean mayPickup(Player pl)
						{
							return false;
						}
					};
			SlotItemHandler itemSave2 = new SlotItemHandler(handler, 3, 112, 30)
					{
						@Override
						public boolean mayPlace(@NotNull ItemStack item)
						{
							return false;
						}
						@Override
						public boolean mayPickup(Player pl)
						{
							return false;
						}
					};
			SlotItemHandler itemSave3 = new SlotItemHandler(handler, 4, 130, 30)
			{
				@Override
				public boolean mayPlace(@NotNull ItemStack item)
				{
					return false;
				}
				@Override
				public boolean mayPickup(Player pl)
				{
					return false;
				}
			};
			SlotItemHandler itemSave4 = new SlotItemHandler(handler, 5, 148, 30)
			{
				@Override
				public boolean mayPlace(@NotNull ItemStack item)
				{
					return false;
				}
				@Override
				public boolean mayPickup(Player pl)
				{
					return false;
				}
			};
			this.addSlot(new SlotItemHandler(handler, 0, 13, 30));
			this.addSlot(outputItemSlot);
			this.addSlot(itemSave1);
			this.addSlot(itemSave2);
			this.addSlot(itemSave3);
			this.addSlot(itemSave4);
			this.addSlot(new SlotItemHandler(handler, 6, 94, 55));
			this.addSlot(new SlotItemHandler(handler, 7, 112, 55));
			this.addSlot(new SlotItemHandler(handler, 8, 130, 55));
			this.addSlot(new SlotItemHandler(handler, 9, 148, 55));
		});
		addDataSlots(data);
	}
	
	//Custom Methods
	private void addPlayerInventory(Inventory plInv)
	{
		for(int i = 0; i < 3; i++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlot(new Slot(plInv, x + i * 9 + 9, 8+ x * 18, 86 + i * 18));
			}
		}
	}
	
	private void addPlayerHotbar(Inventory plInv)
	{
		for(int i =0; i < 9; i++)
		{
			this.addSlot(new Slot(plInv, 1, 8 + i * 18, 144));
		}
	}
	
	//Overrode Methods
	
	//START COPIED CODE
	// CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 10;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
    //END COPIED CODE

	@Override
	public boolean stillValid(Player pl) 
	{
		return stillValid(ContainerLevelAccess.create(world, tileEntity.getBlockPos()), pl, BlockInit.TOUCHSTONE.get());
	}
}
