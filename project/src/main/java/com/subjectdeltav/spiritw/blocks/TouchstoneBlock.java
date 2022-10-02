package com.subjectdeltav.spiritw.blocks;

import java.util.UUID;

import javax.annotation.Nullable;

import com.subjectdeltav.spiritw.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TouchstoneBlock extends Block implements EntityBlock
{

	public TouchstoneBlock(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public InteractionResult use(
			BlockState state, 
			Level world, 
			BlockPos pos, 
			Player player,
			InteractionHand hand,
			BlockHitResult hit)
	{
		return InteractionResult.PASS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		return TileEntityInit.TOUCHSTONE.get().create(pos, state);
	}
	

	
}
