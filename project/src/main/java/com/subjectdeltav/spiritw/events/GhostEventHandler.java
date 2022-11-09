package com.subjectdeltav.spiritw.events;

import com.subjectdeltav.spiritw.effects.ModEffects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Pre;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GhostEventHandler 
{
	//for any events related to the Ghost Mob Effect
	//this handler only maintains the status effect, it does NOT put players into the ghost state. see the death handler and the lantern for that!
	//TODO This is currently causing CTD on load (Currently not init until resolved)
	
	private Minecraft minecraft = Minecraft.getInstance();
	
	
	
	@SubscribeEvent
	protected void hudSupress(Pre event) //cancel health and food hud elements
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
	
	@SubscribeEvent
	protected void pickup(EntityItemPickupEvent event) //prevent item pickup
	{
		Player player  = event.getEntity();
		if(player.hasEffect(ModEffects.GHOST))
		{
			event.setCanceled(true);
		}
	}
}
