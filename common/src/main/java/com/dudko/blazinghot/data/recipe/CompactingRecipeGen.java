package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.ironIngot;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherCompound;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.powderedObsidian;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.foundation.recipe.BlazingStandardRecipeGen;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class CompactingRecipeGen extends BlazingStandardRecipeGen<CompactingRecipe> {

	public CompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	GeneratedRecipe
			STURDY_ALLOY =
			bCreate("sturdy_alloy",
					b -> b
							.requireMultiple(netherCompound(), 2)
							.requireMultiple(powderedObsidian(), 3)
							.require(ironIngot())
							.output(BlazingItems.STURDY_ALLOY)
							.requiresHeat(HeatCondition.SUPERHEATED));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.COMPACTING;
	}
}
