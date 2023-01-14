package com.subjectdeltav.spiritw.loot;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class AddItemModifiers extends LootModifier
{
	public static final Supplier<Codec<AddItemModifiers>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
            .fieldOf("item").forGetter(m -> m.item)).apply(inst, AddItemModifiers::new)));
    private final Item item;
	
	protected AddItemModifiers(LootItemCondition[] conditionsIn, Item item) 
	{
		super(conditionsIn);
		this.item = item;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() 
	{
		return CODEC.get();
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
			LootContext context) 
	{
		generatedLoot.add(new ItemStack(this.item, 1));
		return generatedLoot;
	}

}
