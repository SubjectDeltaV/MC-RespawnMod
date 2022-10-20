 package com.subjectdeltav.spiritw.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TouchstoneScreen extends AbstractContainerScreen<TouchstoneMenu>
{
	//properties
	private static final ResourceLocation TEXTURE = new ResourceLocation(spiritw.MODID, "textures/gui/touchstone.png");
	TouchstoneTile tile;
	
	//Constructor
	public TouchstoneScreen(TouchstoneMenu menu, Inventory inv, Component comp) {
		super(menu, inv, comp);
		tile = this.getMenu().tileEntity;
	}

	
	//Overrides
	@Override
	protected void renderBg(PoseStack stack, float pPartialTick, int mouseX, int mouseY) 
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		blit(stack, x, y, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float delta)
	{
		renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, delta);
		renderTooltip(poseStack, mouseX, mouseY);
	}
	@Override
	protected void init()
	{
		super.init();
		//this.addRenderableWidget(new RegisterPlayerButton(50, 100, tile, tile.player));
		this.addRenderableWidget(new RetrieveItemsButton(100, 100, tile, tile.player));
	}

}
