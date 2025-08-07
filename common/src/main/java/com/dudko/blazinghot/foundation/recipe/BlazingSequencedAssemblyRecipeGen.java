package com.dudko.blazinghot.foundation.recipe;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class BlazingSequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {

	public BlazingSequencedAssemblyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	protected GeneratedRecipe bCreate(String name, UnaryOperator<BlazingSequencedAssemblyRecipeBuilder> transform) {
		GeneratedRecipe
				generatedRecipe =
				c -> transform.apply(new BlazingSequencedAssemblyRecipeBuilder(asResource(name))).build(c);
		all.add(generatedRecipe);
		return generatedRecipe;
	}
}
