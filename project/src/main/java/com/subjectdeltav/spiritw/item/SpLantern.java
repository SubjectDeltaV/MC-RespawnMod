package com.subjectdeltav.spiritw.item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.ModEffects;
import com.subjectdeltav.spiritw.init.EnchantmentInit;
import com.subjectdeltav.spiritw.init.ItemInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import de.maxhenkel.corpse.Main;
import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathManager;
import de.maxhenkel.corpse.corelib.death.PlayerDeathEvent;
import de.maxhenkel.corpse.entities.CorpseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public class SpLantern extends Item 
{
	//NAME: Spirit Lantern
	//Function: This item is held by the player while spirit walking. It enables the ghost effect while spirit walking
	//Also will lift the ghost effect once player right clicks on touchstone. Logic to restore items are still in the event handler
	//TODO create block for placement
	
	//Properties
	protected UUID ownerID; //the UUID for the player owner
	protected TouchstoneTile boundTouchstone; //the Touchstone this lantern is bound to
	public boolean isActive; //only on during SpiritWalking
	protected int playerXP; //will auto-update through a method when being held
	private List<ItemStack> itemsOnDeath;
	public boolean hasItemsToReturn;
	private int tier;
	private BlockPos lastDeathLoc;
	
	
	//Constructor
	public SpLantern(int tier) 
	{
		super(new Item.Properties().tab(ItemInit.ModCreativeTab.instance));
		this.tier = tier;
		lastDeathLoc = new BlockPos(0, 0, 0);
		itemsOnDeath = Collections.emptyList();
		hasItemsToReturn = false;
	}
	
	//Custom Methods
	public void scanAndSaveItems(Player player)
	{
		List<ItemStack> drops = player.getInventory().items;
		for(ItemStack item : drops)
		{
			spiritw.LOGGER.debug("checking item " + item.toString());
			if(item.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) > 0 && item != null)
			{
				spiritw.LOGGER.debug("item has correct enchantment, saving");
				this.itemsOnDeath.add(item);
				hasItemsToReturn = true;
			}else
			{
				spiritw.LOGGER.debug("Item missing correct enchantment, ignoring");
			}
		}
	}
	
	public void SetLastDeathLoc(BlockPos pos)
	{
		this.lastDeathLoc = pos;
	}
	
	public boolean DropCorpse(Player player, boolean setGhost)
	{

		Death death = Death.fromPlayer(player);
		if(!player.level.isClientSide)
		{
			PlayerDeathEvent deathEvent = new PlayerDeathEvent(death, (ServerPlayer) player, DamageSource.OUT_OF_WORLD);
			if(Main.SERVER_CONFIG.maxDeathAge.get() != 0) {
				deathEvent.storeDeath();
			}
			new Thread(() -> deleteOldDeaths(deathEvent.getPlayer().getLevel())).start();
		}
		player.level.addFreshEntity(CorpseEntity.createFromDeath(player, death));
		//player.getInventory().clearContent(); 
		
		if(setGhost)
		{
			int exp = player.totalExperience;
			boolean didSetGhost = SetGhostEffect(player, exp);
			if(!didSetGhost)
			{
				spiritw.LOGGER.debug("An error occurred adding the ghost effect to player");
				return false;
			}
		}
		
		return true;
	}
	
	protected boolean SetGhostEffect(Player player, int xp)
	{
		player.removeAllEffects();
		player.addEffect(new MobEffectInstance(ModEffects.GHOST, 3600));
		player.giveExperiencePoints(xp);
		return true;
	}
	
	public List<ItemStack> getItemsToReturn() //throws Throwable
	{
		if(hasItemsToReturn)
		{
			return this.itemsOnDeath;
		}else
		{
			//TODO insert exception
			spiritw.LOGGER.warn("Attempt to return empty list of items from lantern!");
			return this.itemsOnDeath;
		}
	}
	
	//Method copied from Corpse Mod
    protected static void deleteOldDeaths(ServerLevel serverWorld) {
        int ageInDays = Main.SERVER_CONFIG.maxDeathAge.get();
        if (ageInDays < 0) {
            return;
        }
        long ageInMillis = ((long) ageInDays) * 24L * 60L * 60L * 1000L;

        DeathManager.removeDeathsOlderThan(serverWorld, ageInMillis);
    }
	
	//Overrode Methods
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
		BlockPos blPos = ray.getBlockPos();
		BlockState block = world.getBlockState(blPos);
		Material blockMat = block.getMaterial();
		TouchstoneTile touchstone = (TouchstoneTile) world.getBlockEntity(blPos);
		if(!blockMat.isSolid() && player.hasEffect(ModEffects.WOUNDED))
		{
			//if player is downed and uses the lantern they will die and turn into a ghost
			spiritw.LOGGER.debug("Player has used a lantern while downed, putting into ghost state");
			int xp = player.totalExperience;
			scanAndSaveItems(player);
			boolean didDropCorpse = DropCorpse(player, false);
			if(didDropCorpse)
			{
				spiritw.LOGGER.debug("Dropped Corpse at player location");
				player.giveExperiencePoints(-xp);
				boolean ghostEnabled = SetGhostEffect(player, xp);
				if(ghostEnabled)
				{
					spiritw.LOGGER.debug("Added ghost effect to dead player");
				}else
				{
					spiritw.LOGGER.debug("An Error occured while adding Ghost Effect");
				}
			}else
			{
				spiritw.LOGGER.debug("Error! Corpse not dropped, cannot put into ghost state!");
			}
			
		}else if(touchstone != null && this.hasItemsToReturn)
		{
			ItemStack[] itemsFromDeath = (ItemStack[]) itemsOnDeath.toArray();
			boolean restoreItems = false;
			ItemStack[] itemstoRestore = new ItemStack[4];
			if(itemsFromDeath != null)
			{
				spiritw.LOGGER.debug("A player has interacted with the touchstone with a lantern in hand, checking for any items to restore...");
				ItemStack[] itemsFromTile = touchstone.getSavedItems();

				int toRestoreInd = 0;
				//ItemStack[] removeItemsFromCorpse = new ItemStack[itemsFromTile.length];
				for(int fromDeathInd = 0; fromDeathInd < itemsFromDeath.length; fromDeathInd++)
				{
					ItemStack checkItemFromDeath = itemsFromDeath[fromDeathInd];
					if(checkItemFromDeath != null)
					{
						spiritw.LOGGER.debug("Comparing " + checkItemFromDeath.toString() + " against items in touchstone...");
						for( int fromTileInd = 0; fromTileInd < itemsFromTile.length; fromTileInd++)
						{
							ItemStack checkItemFromTile = itemsFromTile[fromTileInd];
							if(checkItemFromDeath.is(checkItemFromTile.getItem()))
							{
								spiritw.LOGGER.debug("Item Matches, adding it to list to restore items.");
								if(toRestoreInd < itemstoRestore.length)
								{
									itemstoRestore[toRestoreInd] = checkItemFromDeath;
									toRestoreInd++;
									restoreItems = true;
									itemsOnDeath.remove(checkItemFromDeath);
								}else
								{
									spiritw.LOGGER.error("Maximum number of items to restore has been exceeded, not restoring further items");
								}
							}
						}
					} else
					
						spiritw.LOGGER.error("Item to check against touchstone is null, ignoring...");
					}
				}
				if(restoreItems)
				{
					for(int index = 0; index < itemstoRestore.length; index++)
					{
						spiritw.LOGGER.debug("Giving Items to Player...");
						ItemStack giveItem = itemstoRestore[index];
						if(giveItem != null)
						{
							player.addItem(giveItem);
						}
					}
					//itemsRemoveFromCorpse.put(player.getStringUUID(), itemsToRestore.get(player.getStringUUID()).clone());
					//itemsToRestore.remove(player.getStringUUID());

				}

		}
		return super.use(world, player, hand);
	}
}
