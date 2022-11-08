package com.subjectdeltav.spiritw.events;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootHandler 
{
	//this handler is specifically for modifying loot tables

	@SubscribeEvent
	public void loadTables(LootTableLoadEvent event)
	{
		LootTables lootmgr = event.getLootTableManager();
		LootTable table = event.getTable();
		ResourceLocation name = event.getName();
		//if(event.getName().equals(EntityLoot.))
	}
	
}
