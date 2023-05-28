package com.subjectdeltav.spiritw.tiles;

import com.subjectdeltav.spiritw.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Not Yet implemented/WIP
 * @author Mount
 *
 */
public class TouchstoneTile_2 extends TouchstoneTile implements MenuProvider{

	//constructor
	public TouchstoneTile_2(BlockPos pos, BlockState state) 
	{
		super(pos, state);
		this.enchantedItem = false;
		this.currentSavedItemsQ = 0;
		this.data = new ContainerData()
				{
					@Override
					public int get(int index)
					{
						return switch (index)
								{
									case 0 -> TouchstoneTile_2.this.currentSavedItemsQ;
									case 1 -> TouchstoneTile_2.this.maxSavedItems;
									case 2 -> TouchstoneTile_2.this.maxItemsRemaining;
									default -> 0;
								};
					}
					
					@Override
					public void set(int i, int v)
					{
						switch(i)
						{
							case 0 -> TouchstoneTile_2.this.currentSavedItemsQ = v;
							case 1 -> TouchstoneTile_2.this.maxSavedItems = v;
							case 2 -> TouchstoneTile_2.this.maxItemsRemaining = v;
						}
					}
					
					@Override
					public int getCount()
					{
						return 3;
					}
				};
	}

}
