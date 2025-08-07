package com.dudko.blazinghot.data.recipe.v2;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.blazeGoldSheet;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.api.data.recipe.CuttingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class BlazingCuttingRecipeGen extends CuttingRecipeGen {

	public BlazingCuttingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
		super(output, registries, defaultNamespace);
	}

	GeneratedRecipe
			BLAZE_GOLD_SHEET =
			create("blaze_gold_sheet", b -> b.require(blazeGoldSheet()).output(BlazingItems.BLAZE_GOLD_ROD, 2));
}
