package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.item.SpLantern;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, spiritw.MODID);
	
	public static final RegistryObject<Item> SPLANTERN = ITEMS.register("splantern", 
			() -> new SpLantern(1)); //see SpLantern class
	
	public static final RegistryObject<Item> SOUL_GEM = ITEMS.register("soul_gem", 
			() -> new Item( new Item.Properties().tab(ModCreativeTab.instance).stacksTo(16))); //crafting ingredient for bindingstone
	
	public static final RegistryObject<Item> BINDINGSTONE = ITEMS.register("bindingstone", 
			() -> new Item(new Item.Properties().tab(ModCreativeTab.instance).stacksTo(1))); //crafting ingredient for spirit lantern
	
	public static final RegistryObject<Item> SOUL_STONE = ITEMS.register("soul_stone",
			() -> new Item(new Item.Properties().tab(ModCreativeTab.instance).stacksTo(8))); //crafting ingredient for Touchstone

	public static final RegistryObject<Item> SPLANTERN_2 = ITEMS.register("splanten_2", () -> new SpLantern(2)); //create second tier of Spirit Lantern

	public static class ModCreativeTab extends CreativeModeTab {
		public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "spiritwalker");
		private ModCreativeTab(int index, String label) {
			super(index, label);
		}
		
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.BOOK);
		}
	}
}
