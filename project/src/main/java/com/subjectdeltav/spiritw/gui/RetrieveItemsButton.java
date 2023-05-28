package com.subjectdeltav.spiritw.gui;

import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
/**
 * Unused
 * @author Mount
 *
 */

public class RetrieveItemsButton extends Button {

	//Properties
	public static final int HEIGHT = 18;
	public static final int WIDTH = 10;
	TouchstoneTile tileEnt;
	Player player;
	
	public RetrieveItemsButton(int x, int y, TouchstoneTile tile, Player pl) {
		super(x, y, HEIGHT, WIDTH, Component.empty(), null);
		tileEnt = tile;
		player  = pl;
	}
	
	@Override
	public void onPress()
	{
		//tileEnt.getSavedItems((ServerPlayer) player);
	}

}
