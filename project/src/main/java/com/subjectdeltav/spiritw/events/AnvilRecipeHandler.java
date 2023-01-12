package com.subjectdeltav.spiritw.events;

import java.util.ArrayList;

import com.subjectdeltav.spiritw.init.PotionInit;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus  = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilRecipeHandler 
{
	//to handle Anvil recipes
	static class CombineRecipe
	{
		public final Item left;
		public final Item right;
		public final Item out;
		public final int cost;
		protected CombineRecipe(Item left, Item right, Item out, int cost)
		{
			this.left = left;
			this.right = right;
			this.out = out;
			this.cost = cost;
		}
	}
	
	private static ArrayList<CombineRecipe> CombineRecipes = new ArrayList<>();
	
	public static void initAnvilRecipes()
	{
		Item inactivepot_splash = 
				PotionUtils.setPotion(
						new ItemStack(Items.SPLASH_POTION), 
						PotionInit.INACTIVE_REVIVE.get()).getItem();
		Item activepot_splash = 
				PotionUtils.setPotion(
						new ItemStack(Items.SPLASH_POTION), 
						PotionInit.REVIVE_POTION.get()).getItem();
		Item inactivepot = 
				PotionUtils.setPotion(
						new ItemStack(Items.POTION),
						PotionInit.INACTIVE_REVIVE.get()
						).getItem();
		Item activepot = PotionUtils.setPotion(
				new ItemStack(Items.POTION),
				PotionInit.REVIVE_POTION.get()
				).getItem();
		CombineRecipes.add(new CombineRecipe(inactivepot_splash, Items.SOUL_SAND, activepot_splash, 3));
		CombineRecipes.add(new CombineRecipe(inactivepot, Items.SOUL_SAND, activepot, 5));
	}
	
	@SubscribeEvent
	public static void handleCombine(AnvilUpdateEvent event)
	{
		CombineRecipes.forEach(
					(data) -> {
						if(event.getLeft().getItem() == data.left && event.getRight().getItem() == data.right);
						event.setOutput(new ItemStack(data.out, event.getLeft().getCount()));
						event.setCost(data.cost);
						event.setMaterialCost(event.getLeft().getCount()*2);
					}
				);
	}
}
