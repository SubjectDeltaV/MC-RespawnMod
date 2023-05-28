package com.subjectdeltav.spiritw.init;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile_2;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Initializer for our Tile or Block Entities
 * @author Mount
 *
 */
public class TileEntityInit 
{
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES_TYPES = 
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, spiritw.MODID);
	
	public static final RegistryObject<BlockEntityType<TouchstoneTile>> TOUCHSTONE_TILE = 
			TILE_ENTITIES_TYPES.register(
					"touchstone_entity", 
					() -> BlockEntityType.Builder.of(TouchstoneTile::new, 
							BlockInit.TOUCHSTONE.get()).build(null));
	public static final RegistryObject<BlockEntityType<TouchstoneTile_2>> TOUCHSTONE_TILE_2 =
			TILE_ENTITIES_TYPES.register("touchstone_entity_2", 
					() -> BlockEntityType.Builder.of(TouchstoneTile_2::new, 
							BlockInit.TOUCHSTONE_2.get()).build(null));
	
	public static void register(IEventBus eventBus)
	{
		TILE_ENTITIES_TYPES.register(eventBus);
	}
}
