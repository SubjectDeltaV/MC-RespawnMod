package com.subjectdeltav.spiritw.item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.ModEffects;
import com.subjectdeltav.spiritw.init.EnchantmentInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
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
	private final MobEffectInstance ressurectSick;
	private final MobEffectInstance ghostInst;
	private final MobEffectInstance beginGhost;
	private final MobEffect ghost;
	private final MobEffect wounded;
	private final Enchantment bound;
	private List<ItemStack> boundItems_L1;
	
	
	
	//Constructor
	public SpLantern(Properties prop) 
	{
		super(prop);
		this.wounded = ModEffects.WOUNDED;
		this.ghostInst = new MobEffectInstance(ModEffects.GHOST, 360000);
		this.ressurectSick = new MobEffectInstance(ModEffects.RESSURECTION_SICKNESS, 3600);
		this.ghost = ModEffects.GHOST;
		this.beginGhost = new MobEffectInstance(ModEffects.ENTER_GHOST_STATE, 36000);
		this.bound = EnchantmentInit.SPIRITBOUND.get();
	}
	
	//Custom Methods
	protected void scanAndSaveItems(Player player)
	{
		Collection<ItemEntity> drops = player.captureDrops();
		List<ItemStack> allItems = Collections.emptyList();
		for(ItemEntity ent : drops) //convert to list of ItemStack
		{
			ItemStack item = ent.getItem();
			allItems.add(item);
		}
		for(ItemStack item : allItems)
		{
			spiritw.LOGGER.debug("checking item " + item.toString());
			if(item.getEnchantmentLevel(bound) == 0)
			{
				spiritw.LOGGER.debug("item has correct enchantment, saving");
				boundItems_L1.add(item);
			}else
			{
				spiritw.LOGGER.debug("Item missing correct enchantment, ignoring");
			}
		}
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
		if(!blockMat.isSolid() && player.hasEffect(wounded))
		{
			//if player is downed and uses the lantern they will die and turn into a ghost
			spiritw.LOGGER.debug("Player has used a lantern while downed, putting into ghost state");
			int xp = player.totalExperience;
			player.removeAllEffects();
			player.addEffect(beginGhost);
			player.kill();
		} else if(touchstone != null && player.hasEffect(ghost))
		{
			player.removeAllEffects(); //remove the ghost and any related effects
			player.setHealth(20);
			player.setInvisible(false); //remove the invisibility granted from the effect
			player.addEffect(new MobEffectInstance(ModEffects.RESSURECTION_SICKNESS, 3600));
		}
		return super.use(world, player, hand);
	}
}
