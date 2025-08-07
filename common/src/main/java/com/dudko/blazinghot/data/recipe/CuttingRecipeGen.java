package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.blazeGoldSheet;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class CuttingRecipeGen extends com.simibubi.create.api.data.recipe.CuttingRecipeGen {

	public CuttingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	GeneratedRecipe
			BLAZE_GOLD_SHEET =
			create("blaze_gold_sheet", b -> b.require(blazeGoldSheet()).output(BlazingItems.BLAZE_GOLD_ROD, 2));
}
