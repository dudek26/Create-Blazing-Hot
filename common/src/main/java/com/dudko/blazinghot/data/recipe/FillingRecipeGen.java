package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount.NUGGET_COVER;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.apple;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.carrot;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.lava;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.melon;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.netherDough;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.BlazingStandardRecipeGen;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.util.ItemUtil;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class FillingRecipeGen extends BlazingStandardRecipeGen<FillingRecipe> {

	public FillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	GeneratedRecipe
			GLISTERING_MELON =
			bCreate("glistering_melon",
					b -> b.require(moltenGold(), NUGGET_COVER).require(melon()).output(Items.GLISTERING_MELON_SLICE)),

	GOLDEN_APPLE = metalApple(BlazingMetals.GOLD, Items.GOLDEN_APPLE),
			GOLDEN_CARROT =
					metalCarrot(BlazingMetals.GOLD, Items.GOLDEN_CARROT),
			BLAZE_CARROT =
					metalCarrot(BlazingMetals.BLAZE_GOLD, BlazingItems.BLAZE_CARROT),
			BLAZE_APPLE =
					metalApple(BlazingMetals.BLAZE_GOLD, BlazingItems.BLAZE_APPLE),
			IRON_CARROT =
					metalCarrot(BlazingMetals.IRON, BlazingItems.IRON_CARROT),
			IRON_APPLE =
					metalApple(BlazingMetals.IRON, BlazingItems.IRON_APPLE),
			BRASS_CARROT =
					metalCarrot(BlazingMetals.BRASS, BlazingItems.BRASS_CARROT),
			BRASS_APPLE =
					metalApple(BlazingMetals.BRASS, BlazingItems.BRASS_APPLE),
			COPPER_CARROT =
					metalCarrot(BlazingMetals.COPPER, BlazingItems.COPPER_CARROT),
			COPPER_APPLE =
					metalApple(BlazingMetals.COPPER, BlazingItems.COPPER_APPLE),
			ZINC_CARROT =
					metalCarrot(BlazingMetals.ZINC, BlazingItems.ZINC_CARROT),
			ZINC_APPLE =
					metalApple(BlazingMetals.ZINC, BlazingItems.ZINC_APPLE),
			BLAZE_ROLL =
					bCreate("blaze_roll",
							b -> b
									.require(lava(), MultiAmount.BOTTLE)
									.require(netherDough())
									.output(BlazingItems.BLAZE_ROLL));

	private GeneratedRecipe metalApple(BlazingMetal metal, ItemLike result) {
		return bCreate(ItemUtil.getItemID(result).getPath(),
				b -> b.require(metal.getFluidTag(), MultiAmount.INGOT_COVER).require(apple()).output(result));
	}

	private GeneratedRecipe metalCarrot(BlazingMetal metal, ItemLike result) {
		return bCreate(ItemUtil.getItemID(result).getPath(),
				b -> b.require(metal.getFluidTag(), NUGGET_COVER).require(carrot()).output(result));
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.FILLING;
	}
}
