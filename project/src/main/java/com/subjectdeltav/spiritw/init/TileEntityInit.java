package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.blocks.TouchstoneBlock;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityInit 
{
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES_TYPES = 
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, spiritw.MODID);
	
	public static final RegistryObject<BlockEntityType<TouchstoneTile>> TOUCHSTONE = 
			TILE_ENTITIES_TYPES.register(
					"touchstone", 
					() -> BlockEntityType.Builder.of(TouchstoneTile::new, 
							BlockInit.TOUCHSTONE.get()).build(null));
}
