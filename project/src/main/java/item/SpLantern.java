package item;

import java.util.UUID;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.effects.ModEffects;
import com.subjectdeltav.spiritw.init.EffectInit;
import com.subjectdeltav.spiritw.tiles.TouchstoneTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public class SpLantern extends Item 
{
	//NAME: Spirit Lantern
	//Function: This item is held by the player while spirit walking. It will enable the effect for spirit walking (TODO) 
	//TODO create block for placement
	
	//Properties
	protected UUID ownerID; //the UUID for the player owner
	protected TouchstoneTile boundTouchstone; //the Touchstone this lantern is bound to
	public boolean isActive; //only on during SpiritWalking
	protected int playerXP; //will auto-update through a method when being held
	private final MobEffectInstance ressurectSick;
	private final MobEffectInstance ghost;
	private final MobEffect wounded;
	
	
	
	//Constructor
	public SpLantern(Properties prop) 
	{
		super(prop);
		this.wounded = ModEffects.WOUNDED;
		this.ghost = new MobEffectInstance(ModEffects.GHOST, 360000);
		this.ressurectSick = new MobEffectInstance(ModEffects.RESSURECTION_SICKNESS, 3600);
	}
	
	//Custom Methods
	
	//Overrode Methods
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
		BlockPos blPos = ray.getBlockPos();
		BlockState block = world.getBlockState(blPos);
		Material blockMat = block.getMaterial();
		if(!blockMat.isSolid() && player.hasEffect(wounded))
		{
			//if player is downed and uses the lantern they will die and turn into a ghost
			spiritw.LOGGER.debug("Player has used a lantern while downed, putting into ghost state");
			int xp = player.totalExperience;
			player.removeAllEffects();
			player.respawn();
			player.teleportTo(blPos.getX(), blPos.getY() + 1, blPos.getZ());
			this.isActive = true;
			player.giveExperiencePoints(xp);
			player.addEffect(ghost);
		}
		return super.use(world, player, hand);
	}
}
