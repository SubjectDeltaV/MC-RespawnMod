package com.subjectdeltav.spiritw.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class RegisterPlayerButton extends Button {

	//Properties
	public static final int WIDTH = 18;
	public static final int HEIGHT = 10;
	TouchstoneTile tileEnt;
	Player player;
	
	//Constructor
	public RegisterPlayerButton(int x, int y, TouchstoneTile tile, Player pl) 
	{
		super(x, y, WIDTH, HEIGHT, Component.empty(), null);
		tileEnt = tile;
	}
	
	@Override
	public void onPress()
	{
		tileEnt.regsiterPlayer(player);
	}
}
