package com.subjectdeltav.spiritw.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class TouchstoneMenu extends AbstractContainerMenu {

	//properties
	
	//Constructor
	protected TouchstoneMenu(MenuType<?> p_38851_, int p_38852_) {
		super(p_38851_, p_38852_);
		// TODO Auto-generated constructor stub
	}
	
	//Overrode Methods
	@Override
	public ItemStack quickMoveStack(Player pl, int x)
	{
		return null;
	}

	@Override
	public boolean stillValid(Player pl) {
		// TODO Auto-generated method stub
		return false;
	}
}
