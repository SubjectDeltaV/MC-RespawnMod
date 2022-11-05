package com.subjectdeltav.spiritw.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TouhcstoneContainer implements Container{

	@Override
	public void clearContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getContainerSize() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getItem(int p_18941_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_18951_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setItem(int slot, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean stillValid(Player p_18946_) {
		// TODO Auto-generated method stub
		return true;
	}

}
