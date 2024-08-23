package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.blazeGoldSheet;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class CuttingRecipeGen extends BlazingProcessingRecipeGen {

	public CuttingRecipeGen(PackOutput output) {
		super(output);
	}

	GeneratedRecipe
			BLAZE_GOLD_SHEET =
			create("blaze_gold_sheet", b -> b.require(blazeGoldSheet()).output(BlazingItems.BLAZE_GOLD_ROD, 2));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.CUTTING;
	}
}
