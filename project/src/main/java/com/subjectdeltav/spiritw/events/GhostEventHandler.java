package com.subjectdeltav.spiritw.events;

import com.subjectdeltav.spiritw.effects.ModEffects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Pre;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handler for any events related to the Ghost Mob Effect
 * this handler only maintains the status effect, it does 
 * NOT put players into the ghost state. see the death handler 
 * and the lantern for that!
 * @author Mount
 *
 */
public class GhostEventHandler 
{
	//
	//
	
	private Minecraft minecraft = Minecraft.getInstance();
	
	
	
	@SubscribeEvent
	/**
	 * cancel health and food hud elements
	 * @param event
	 */
	protected void hudSupress(Pre event) 
	{
		Player player = minecraft.player;
		if(event.getOverlay().equals(VanillaGuiOverlay.PLAYER_HEALTH.type()) && player.hasEffect(ModEffects.GHOST))
		{
			event.setCanceled(true);
		}else if(event.getOverlay().equals(VanillaGuiOverlay.FOOD_LEVEL.type()) && player.hasEffect(ModEffects.GHOST))
		{
			event.setCanceled(true);
		}
	}
	
	/**
	 * This will auto-cancel the item pickup event
	 * @param event
	 */
	@SubscribeEvent
	protected void pickup(EntityItemPickupEvent event) //prevent item pickup
	{
		Player player  = event.getEntity();
		if(player.hasEffect(ModEffects.GHOST))
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	protected void throwPotion(RightClickItem event) //prevent potions from being thrown
	{
		ItemStack item = event.getItemStack();
		Player player = event.getEntity();
		if(item.getItem().equals(Items.SPLASH_POTION) && player.hasEffect(ModEffects.GHOST))
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	protected void interaction(LeftClickBlock event) //prevent left clicking of blocks, no breaking of any kind!)
	{
		Player player = event.getEntity();
		if(player.hasEffect(ModEffects.GHOST))
		{
			event.setCanceled(true);
		}
	}
	
	/**
	 * Prevents players from opening inventories while a ghost
	 * (Does this include there inventory?)
	 * @param event
	 */
	@SubscribeEvent
	protected void openInv(PlayerContainerEvent event)
	{
		Player player = event.getEntity();
		if(player.hasEffect(ModEffects.GHOST))
		{
			event.setCanceled(true);
		}
	}
}
