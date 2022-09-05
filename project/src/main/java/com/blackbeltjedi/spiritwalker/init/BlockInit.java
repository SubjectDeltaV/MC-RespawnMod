package com.blackbeltjedi.spiritwalker.init;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import com.blackbeltjedi.spiritwalker.SpiritWalker;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SpiritWalker.MODID);
	public static final RegistryObject<Block> TOUCHSTONE = BLOCKS.register("Touchstone", () -> new Block(Block.Properties.of(Material.STONE).strength(4f, 1200f).lightLevel((state) -> 15)));
	
	@SubscribeEvent
	public static void onRegisterItems(final RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
			BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
				Block block = blockRegistryObject.get();
				Supplier<Item> blockItemFactory = () -> new BlockItem(block, null);
				event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
			});
		}
	}
}
