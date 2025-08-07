package com.dudko.blazinghot.foundation.recipe;

import java.util.concurrent.CompletableFuture;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class BlazingProcessingRecipeGen<R extends StandardProcessingRecipe<?>> extends StandardProcessingRecipeGen<R> {

	public BlazingProcessingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
		super(output, registries, defaultNamespace);
	}

	@Override
	protected BlazingRecipeBuilder<R> getBuilder(ResourceLocation id) {
		return new BlazingRecipeBuilder<>(getSerializer().factory(), id);
	}
}
