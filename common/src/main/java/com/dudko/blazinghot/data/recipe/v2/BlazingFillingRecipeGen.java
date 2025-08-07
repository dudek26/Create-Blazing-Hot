package com.dudko.blazinghot.data.recipe.v2;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.apple;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.carrot;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.lava;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.melon;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherDough;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.data.recipe.BlazingRecipeProvider;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class BlazingFillingRecipeGen extends FillingRecipeGen {

	public BlazingFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
		super(output, registries, defaultNamespace);
	}

	BlazingRecipeProvider.GeneratedRecipe
			GLISTERING_MELON =
			create("glistering_melon",
					b -> b.require(melon()).require(moltenGold(), NUGGET_COVER).output(Items.GLISTERING_MELON_SLICE)),
			GOLDEN_APPLE =
					metalApple(BlazingMetals.GOLD, Items.GOLDEN_APPLE),
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
					create("blaze_roll",
							b -> b
									.require(netherDough())
									.require(lava(), MultiAmount.BOTTLE.get())
									.output(BlazingItems.BLAZE_ROLL));

	private BlazingRecipeProvider.GeneratedRecipe metalApple(BlazingMetal metal, ItemLike result) {
		return create(result.asItem().toString(),
				b -> b.require(apple()).require(metal.getFluidTag(), MultiAmount.INGOT_COVER).output(result));
	}

	private BlazingRecipeProvider.GeneratedRecipe metalCarrot(BlazingMetal metal, ItemLike result) {
		return create(result.asItem().toString(),
				b -> b.require(carrot()).require(metal.getFluidTag(), MultiAmount.NUGGET_COVER).output(result));
	}

}
