package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherCompound;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class HauntingRecipeGen extends com.simibubi.create.api.data.recipe.HauntingRecipeGen {

	public HauntingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	GeneratedRecipe
			NETHER_COMPOUND =
			create("nether_compound", b -> b.require(netherCompound()).output(BlazingItems.NETHER_ESSENCE));

}
