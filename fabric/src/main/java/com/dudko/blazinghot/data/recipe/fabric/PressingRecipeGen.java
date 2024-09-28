package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.blazeGoldIngot;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class PressingRecipeGen extends BlazingProcessingRecipeGen {

	public PressingRecipeGen(PackOutput output) {
		super(output);
	}

	GeneratedRecipe
			BLAZE_GOLD =
			create("blaze_gold_ingot", b -> b.require(blazeGoldIngot()).output(BlazingItems.BLAZE_GOLD_SHEET.get())),
			STURDY_ALLOY =
					create("sturdy_alloy", b -> b.require(BlazingItems.STURDY_ALLOY).output(AllItems.STURDY_SHEET));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.PRESSING;
	}
}
