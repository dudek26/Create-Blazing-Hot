package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.netherCompound;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class HauntingRecipeGen extends BlazingProcessingRecipeGen {

	public HauntingRecipeGen(PackOutput output) {
		super(output);
	}

	GeneratedRecipe
			NETHER_COMPOUND =
			create("nether_compound", b -> b.require(netherCompound()).output(BlazingItems.NETHER_ESSENCE));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.HAUNTING;
	}
}
