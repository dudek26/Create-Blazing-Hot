package com.dudko.blazinghot.foundation.recipe;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class BlazingStandardRecipeGen<R extends StandardProcessingRecipe<?>> extends StandardProcessingRecipeGen<R> {
	
	public BlazingStandardRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	@Override
	protected BlazingStandardRecipeBuilder<R> getBuilder(ResourceLocation id) {
		return new BlazingStandardRecipeBuilder<>(getSerializer().factory(), id);
	}

	/**
	 * Helper method for using {@link BlazingStandardRecipeBuilder}
	 *
	 * @see ProcessingRecipeGen#createWithDeferredId
	 */
	protected GeneratedRecipe bCreateWithDeferredId(Supplier<ResourceLocation> name, Function<BlazingStandardRecipeBuilder<R>, ? extends StandardProcessingRecipe.Builder<R>> transform) {
		GeneratedRecipe generatedRecipe = c -> transform.apply(getBuilder(name.get())).build(c);
		all.add(generatedRecipe);
		return generatedRecipe;
	}

	/**
	 * Helper method for using {@link BlazingStandardRecipeBuilder}
	 *
	 * @see ProcessingRecipeGen#create(String, UnaryOperator)
	 */
	protected GeneratedRecipe bCreate(String name, Function<BlazingStandardRecipeBuilder<R>, ? extends StandardProcessingRecipe.Builder<R>> transform) {
		return bCreate(asResource(name), transform);
	}

	/**
	 * Helper method for using {@link BlazingStandardRecipeBuilder}
	 *
	 * @see ProcessingRecipeGen#create(ResourceLocation, UnaryOperator)
	 */
	protected GeneratedRecipe bCreate(ResourceLocation name, Function<BlazingStandardRecipeBuilder<R>, ? extends StandardProcessingRecipe.Builder<R>> transform) {
		return bCreateWithDeferredId(() -> name, transform);
	}

}
