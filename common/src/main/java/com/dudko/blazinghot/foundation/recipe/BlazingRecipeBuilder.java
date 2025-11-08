package com.dudko.blazinghot.foundation.recipe;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public abstract class BlazingRecipeBuilder<P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, S extends BlazingRecipeBuilder<P, R, S>> extends ProcessingRecipeBuilder<P, R, S> implements IBlazingRecipeBuilder<P, R, S> {

	protected final List<LoadCondition<?>> loadConditions;

	public BlazingRecipeBuilder(ProcessingRecipe.Factory<P, R> factory, ResourceLocation recipeId) {
		super(factory, recipeId);
		loadConditions = new ArrayList<>();
	}

	@Override
	public S require(SizedFluidIngredient fluidIngredient) {
		return super.require(fluidIngredient);
	}

	@Override
	public S output(Fluid fluid, MultiAmount amount) {
		return IBlazingRecipeBuilder.fluidOutput(self(), fluid, amount);
	}

	@Override
	public S requireMultiple(Ingredient ingredient, int amount) {
		for (int i = 0; i < amount; i++) {
			require(ingredient);
		}
		return self();
	}

	@Override
	public S withConditions(List<LoadCondition<?>> conditions) {
		loadConditions.addAll(conditions);
		return self();
	}

	@Override
	public List<LoadCondition<?>> getLoadConditions() {
		return loadConditions;
	}

	@Override
	public S require(Ingredient ingredient) {
		return super.require(ingredient);
	}

	@Override
	public ResourceLocation getRecipeId() {
		return recipeId;
	}

	@Override
	public void build(RecipeOutput consumer) {
		IBlazingRecipeBuilder.super.build(consumer);
	}

}
