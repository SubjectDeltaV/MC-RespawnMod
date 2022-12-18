package com.subjectdeltav.spiritw.item;

import java.util.ArrayList;
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
	private ItemStack[] itemsOnDeath;
	protected boolean hasItemsToReturn;
	private int tier;
	private BlockPos lastDeathLoc;
	public ItemStack thisStack;
	
	
	//Constructor
	public SpLantern(int tier) 
	{
		super(new Item.Properties().tab(ItemInit.ModCreativeTab.instance));
		this.tier = tier;
		lastDeathLoc = new BlockPos(0, 0, 0);
		hasItemsToReturn = false;
		itemsOnDeath = new ItemStack[4];
	}
	
	//Custom Methods
	public List<ItemStack> scanAndSaveItems(Player player)
	{
		if(!player.level.isClientSide)
		{
			List<ItemStack> drops = player.getInventory().items;
			itemsOnDeath = new ItemStack[drops.size()];
			List<ItemStack> lanterns = new ArrayList();
			int index = 0;
			for(ItemStack item : drops)
			{
				try
				{
					spiritw.LOGGER.debug("checking item " + item.toString());
					if(item.getEnchantmentLevel(EnchantmentInit.SPIRITBOUND.get()) > 0 && item != null)
					{
						spiritw.LOGGER.debug("item has correct enchantment, saving");
						itemsOnDeath[index] = item;
						hasItemsToReturn = true;
					}else if(item.getItem() instanceof SpLantern)
					{
						lanterns.add(item);
					}else
					{
						spiritw.LOGGER.debug("Item missing correct enchantment, ignoring");
					}
					index++;
				}catch(Exception e)
				{
					spiritw.LOGGER.error("Exception Caught: propable cause is trying to store items from death in array but array is too small");
					e.printStackTrace();
				}
			}
			return lanterns;
		}
		return null;
	}
	
	public void SetLastDeathLoc(BlockPos pos)
	{
		this.lastDeathLoc = pos;
	}
	
	public boolean DropCorpse(Player player, List<ItemStack> lanterns, boolean setGhost) throws Exception
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
		player.getInventory().clearContent(); 
		player.level.addFreshEntity(CorpseEntity.createFromDeath(player, death));
		
		//give lanterns back
		for(ItemStack lantern : lanterns)
		{
			player.addItem(lantern);
		}
		
		this.SetLastDeathLoc(player.blockPosition()); 
		if(setGhost)
		{
			int exp = player.totalExperience;
			boolean didSetGhost = SetGhostEffect(player, exp);
			if(!didSetGhost)
			{
				throw new Exception("Error Adding Ghost Effect to Player");
			}
		}
		return true;
	}
	
	protected boolean SetGhostEffect(Player player, int xp)
	{
		player.removeAllEffects();
		player.setSecondsOnFire(0);
		player.addEffect(new MobEffectInstance(ModEffects.GHOST, 3600));
		player.setSecondsOnFire(0);
		player.giveExperiencePoints(xp);
		return true;
	}
	
	public ItemStack[] getItemsToReturn() throws Exception
	{
		if(hasItemsToReturn)
		{
			return this.itemsOnDeath;
		}else
		{
			throw new Exception("Tryed to restore ItemsOnDeath to player while array is empty");
		}
	}
	
	public boolean getSavedStatus()
	{
		return hasItemsToReturn;
	}
	
	public boolean clearSavedItems()
	{
		itemsOnDeath = null; //empty out the array
		hasItemsToReturn = false;
		return true;
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
			List<ItemStack> lanterns = Collections.emptyList();
			try
			{
				lanterns = scanAndSaveItems(player);
			}catch(Exception e)
			{
				spiritw.LOGGER.error("Unkown error occured scanning player items");
				e.printStackTrace();
			}
			scanAndSaveItems(player);
			boolean didDropCorpse = false;
			try {
				didDropCorpse = DropCorpse(player, lanterns, false);
			} catch (Exception e) {
				spiritw.LOGGER.error("Error! Corpse not dropped, cannot put into ghost state!");
				e.printStackTrace();
			}
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
					spiritw.LOGGER.error("An Error occured while adding Ghost Effect");
				}
			}
		}
		return super.use(world, player, hand);
	}
}
