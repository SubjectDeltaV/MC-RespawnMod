package com.subjectdeltav.spiritw.tiles;

import java.util.UUID;

import com.subjectdeltav.spiritw.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TouchstoneTile extends BlockEntity {

	public TouchstoneTile(BlockPos pos, BlockState state) {
		super(TileEntityInit.TOUCHSTONE.get(), pos, state);
		// TODO Auto-generated constructor stub
	}
	
	UUID PlayerID;
	ItemStack item1;
	ItemStack item2;
	ItemStack item3;
	
	
}
