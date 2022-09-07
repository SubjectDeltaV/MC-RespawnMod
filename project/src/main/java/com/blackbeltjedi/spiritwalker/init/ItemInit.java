package com.blackbeltjedi.spiritwalker.init;

import com.blackbeltjedi.spiritwalker.spiritwalker;

import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, spiritwalker.MODID);
	
	public static final RegistryObject<Item> LANTERN = ITEMS.register("lantern", () -> new Item (new Properties().tab(ModCreativeTab.instance)));
	
	public static class ModCreativeTab extends CreativeModeTab {
		public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "spiritwalker");
		private ModCreativeTab(int index, String label) {
			super(index, label);
	}

		@Override
		public ItemStack makeIcon() {
			// TODO Auto-generated method stub
			return new ItemStack(LANTERN.get());
		}
		
	}
}
