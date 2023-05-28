package com.subjectdeltav.spiritw.events;

import com.subjectdeltav.spiritw.spiritw;
import com.subjectdeltav.spiritw.init.ItemInit;
import com.subjectdeltav.spiritw.init.PotionInit;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Potions Recipes Must be Added manually at runtime
 * This will contain all the recipes we will add to the Brewing Stand
 * @author Mount
 *
 */
public class PotionRecipeHandler 
{
	//To Contain All Logic for adding recipes to Brewing Stand

	public static void Potions(FMLCommonSetupEvent event)
	{
		BrewingRecipeRegistry.addRecipe(
				Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
				Ingredient.of(Items.SOUL_SAND),
				PotionUtils.setPotion(new ItemStack(Items.POTION), PotionInit.INACTIVE_REVIVE.get()));
		BrewingRecipeRegistry.addRecipe(
				Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)),
				Ingredient.of(Items.SOUL_SAND),
				PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), PotionInit.INACTIVE_REVIVE.get()));
		spiritw.LOGGER.debug("Added Brewing Recipes");
	}
}
