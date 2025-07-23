package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.ironIngot;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherCompound;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.powderedObsidian;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class CompactingRecipeGen extends BlazingProcessingRecipeGen {

	public CompactingRecipeGen(PackOutput output) {
		super(output);
	}

	GeneratedRecipe
			STURDY_ALLOY =
			create("sturdy_alloy",
					b -> b
							.requireMultiple(netherCompound(), 2)
							.require(powderedObsidian())
							.require(ironIngot())
							.output(BlazingItems.STURDY_ALLOY)
							.requiresHeat(HeatCondition.SUPERHEATED));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.COMPACTING;
	}

}
