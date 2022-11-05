package com.subjectdeltav.spiritw.init;

import java.util.function.Supplier;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.blocks.TouchstoneBlock;
import com.subjectdeltav.spiritw.init.ItemInit.ModCreativeTab;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, spiritw.MODID);
	
	public static final RegistryObject<Block> TOUCHSTONE = BLOCKS.register("touchstone", 
			() -> new TouchstoneBlock(Block.Properties.of(Material.STONE).strength(4f, 1200f).lightLevel((state) -> 15)));
	
	
	//automatically create items for blocks
	@SubscribeEvent
	public static void onRegisterItem(final RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
			BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
				Block block = blockRegistryObject.get();
				Item.Properties properties = new Item.Properties().tab(ModCreativeTab.instance);
				Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);
				event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
			});
		}
	}
	public static void register(IEventBus eventBus)
	{
		BLOCKS.register(eventBus);
	}
}
