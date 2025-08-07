package com.dudko.blazinghot.foundation.recipe;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public abstract class BlazingRecipeGen<P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, B extends BlazingRecipeBuilder<P, R, B>> extends ProcessingRecipeGen<P, R, B> {

	public BlazingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

}
