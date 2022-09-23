package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.spiritw;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, spiritw.MODID);
	
	public static final RegistryObject<Item> SPLANTERN = ITEMS.register("splantern", 
			() -> new Item(new Item.Properties().tab(ModCreativeTab.instance)));

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
