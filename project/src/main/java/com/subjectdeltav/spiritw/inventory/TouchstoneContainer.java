package com.subjectdeltav.spiritw.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TouchstoneContainer implements Container{

	@Override
	public void clearContent() {
		// Auto-generated method stub
		
	}

	@Override
	public int getContainerSize() {
		// Auto-generated method stub
		return 10;
	}

	@Override
	public boolean isEmpty() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getItem(int p_18941_) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_18951_) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setItem(int slot, ItemStack item) {
		// Auto-generated method stub
		
	}

	@Override
	public void setChanged() {
		// Auto-generated method stub
		
	}

	@Override
	public boolean stillValid(Player p_18946_) {
		//  Auto-generated method stub
		return true;
	}

}
