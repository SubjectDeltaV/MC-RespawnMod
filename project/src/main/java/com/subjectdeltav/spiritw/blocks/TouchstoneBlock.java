package com.subjectdeltav.spiritw.blocks;

import javax.annotation.Nullable;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.ModEffects;
import com.subjectdeltav.spiritw.init.TileEntityInit;
import com.subjectdeltav.spiritw.item.SpLantern;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class TouchstoneBlock extends BaseEntityBlock
{
	//properties
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 10, 16);
	public int tier;
	
	//CONSTRUCTOR
	public TouchstoneBlock(Properties properties, int tier) 
	{
		super(properties);
		this.tier = tier;
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
			ItemStack item = pl.getItemInHand(hand);
			SpLantern lantern = null;
			TouchstoneTile tile = null;
			if(entity instanceof TouchstoneTile && pl.hasEffect(ModEffects.GHOST))
			{
				spiritw.LOGGER.debug("Player interacted with Touchstone while a ghost, Checking if need to restore Items");
				try
				{
					lantern = (SpLantern) item.getItem();
					tile = (TouchstoneTile) entity;
					
				}catch(ClassCastException e)
				{
					spiritw.LOGGER.error("Casting Error");
					e.printStackTrace();
				}
				if(lantern.getSavedStatus())
				{
					spiritw.LOGGER.debug("Lantern has items bound to it, attempting to restore...");
					try
					{
						boolean didReturn = tile.scanForAndReturnItems(pl, lantern);
						if(didReturn)
						{
							spiritw.LOGGER.debug("Success!");
						}
					}catch(Exception e)
					{
						spiritw.LOGGER.error("Error. Unable to return items.");
						e.printStackTrace();
					}
				}
			}
			if(entity instanceof TouchstoneTile)
			{
				try
				{
					tile = (TouchstoneTile) entity;
				}catch(Exception e)
				{
					spiritw.LOGGER.error("Casting Error");
					e.printStackTrace();
				}
				
				if(tile.playerIsSet)
				{
					//ItemStack holdingMain = pl.getMainHandItem();
					//ItemStack holdingOff = pl.getOffhandItem();
					if(tile.getPlayerID() == pl.getUUID())
					{
						if(tier == 1)
						{
							spiritw.LOGGER.debug("Player Owner is trying to open Touchstone, Setting XP and opening UI...");
							NetworkHooks.openScreen(((ServerPlayer) pl), (TouchstoneTile) entity, pos);							
						}else
						{
							spiritw.LOGGER.debug("Touchstone has invalid tier presnet, ignoring request to open");
						}
					}else
					{
						spiritw.LOGGER.debug("Player attempted to open Touchstone that isn't theirs...ignoring");
					}
				}else
				{
					spiritw.LOGGER.debug("No Player owner set for this touchstone, setting owner and opening UI");
					tile.setPlayer(pl);
					NetworkHooks.openScreen(((ServerPlayer) pl), (TouchstoneTile) entity, pos);
				}

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
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter get, BlockPos pos, CollisionContext cont)
	{
		return SHAPE;
	}
}
