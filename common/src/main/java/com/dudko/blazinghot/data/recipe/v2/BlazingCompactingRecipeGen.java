package com.dudko.blazinghot.data.recipe.v2;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.ironIngot;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherCompound;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.powderedObsidian;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class BlazingCompactingRecipeGen extends CompactingRecipeGen {

	public BlazingCompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
		super(output, registries, defaultNamespace);
	}

	GeneratedRecipe
			STURDY_ALLOY =
			create("sturdy_alloy",
					b -> b
							.require(netherCompound())
							.require(netherCompound())
							.require(powderedObsidian())
							.require(powderedObsidian())
							.require(powderedObsidian())
							.require(ironIngot())
							.output(BlazingItems.STURDY_ALLOY)
							.requiresHeat(HeatCondition.SUPERHEATED));

}
