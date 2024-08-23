package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.blazeGoldSheet;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.copperCasing;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class ItemApplicationRecipeGen extends BlazingProcessingRecipeGen {

	public ItemApplicationRecipeGen(PackOutput output) {
		super(output);
	}

	GeneratedRecipe
			BLAZE_CASING =
			create("blaze_casing_from_log",
					b -> b.require(copperCasing()).require(blazeGoldSheet()).output(BlazingBlocks.BLAZE_CASING));


	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.ITEM_APPLICATION;
	}
}
