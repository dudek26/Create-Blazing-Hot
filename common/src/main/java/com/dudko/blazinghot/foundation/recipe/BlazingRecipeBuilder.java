package com.dudko.blazinghot.foundation.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazingRecipeBuilder<R extends StandardProcessingRecipe<?>> extends StandardProcessingRecipe.Builder<R> {

	protected final List<LoadCondition<?>> loadConditions;

	public BlazingRecipeBuilder(StandardProcessingRecipe.Factory<R> factory, ResourceLocation recipeId) {
		super(factory, recipeId);
		loadConditions = new ArrayList<>();
	}

	public BlazingRecipeBuilder<R> withCondition(LoadCondition<?> condition) {
		loadConditions.add(condition);
		return self();
	}

	@Override
	public BlazingRecipeBuilder<R> self() {
		return this;
	}

	@Override
	public void build(RecipeOutput consumer) {
		super.build(consumer);
	}
}
