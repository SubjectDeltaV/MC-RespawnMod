package com.subjectdeltav.spiritw.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.subjectdeltav.spiritw.spiritw;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TouchstoneScreen extends AbstractContainerScreen<TouchstoneMenu>
{
	//properties
	private static final ResourceLocation TEXTURE = new ResourceLocation(spiritw.MODID, "textures/gui/touchstone_gui.png");
	
	//Constructor
	public TouchstoneScreen(TouchstoneMenu menu, Inventory inv, Component comp) {
		super(menu, inv, comp);
		// TODO Auto-generated constructor stub
	}

	
	//Overrides
	@Override
	protected void renderBg(PoseStack stack, float pPartialTick, int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float delta)
	{
		renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, delta);
		renderTooltip(poseStack, mouseX, mouseY);
	}

}
