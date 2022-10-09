package com.subjectdeltav.spiritw.blocks;

import java.util.UUID;

import javax.annotation.Nullable;

import com.subjectdeltav.spiritw.init.TileEntityInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class TouchstoneBlock extends BaseEntityBlock
{
	//properties

	
	//CONSTRUCTOR
	public TouchstoneBlock(Properties properties) 
	{
		super(properties);
	}

	//custom methods
	//overrode methods
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) //connect to TileEntity class
	{
		return new TouchstoneTile(pos, state);
	} 
	
	@Override
	public void onRemove(BlockState state, Level l, BlockPos pos, BlockState newState, boolean isMoving) //setup drops on destroy, will need additional logic to exclude saved items, since we instatiate a copy of them instead
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockEntity entity = l.getBlockEntity(pos);
			if(entity instanceof TouchstoneTile) //may need to switch to get method from registry
			{
				((TouchstoneTile) entity).drops();
			}
		}
		super.onRemove(state, l, pos, newState, isMoving);
	} 
	
	@Override
	public RenderShape getRenderShape(BlockState state) //ensure model is not invis
	{
		return RenderShape.MODEL;
	} 
	
	@Override 
	public InteractionResult use(BlockState state, Level l, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult hit)
	{
		if (!l.isClientSide())
		{
			BlockEntity entity = l.getBlockEntity(pos);
			if(entity instanceof TouchstoneTile)
			{
				NetworkHooks.openScreen(((ServerPlayer) pl), (TouchstoneTile) entity, pos);
			} else
			{
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}
		
		return InteractionResult.sidedSuccess(l.isClientSide());
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, TileEntityInit.TOUCHSTONE_TILE.get(), TouchstoneTile::tick);
	}
}
