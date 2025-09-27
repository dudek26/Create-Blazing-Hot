package com.dudko.blazinghot.foundation.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazingStandardRecipeBuilder<R extends StandardProcessingRecipe<?>> extends StandardProcessingRecipe.Builder<R> implements IBlazingRecipeBuilder<ProcessingRecipeParams, R, BlazingStandardRecipeBuilder<R>> {

	protected final List<LoadCondition<?>> loadConditions;

	/**
	 * TODO: Remove after <a href="https://github.com/Creators-of-Create/Create/pull/9070">#9070</a> is merged
	 */
//	protected FluidIngredient mixerFuel;
	public BlazingStandardRecipeBuilder(StandardProcessingRecipe.Factory<R> factory, ResourceLocation recipeId) {
		super(factory, recipeId);
		this.loadConditions = new ArrayList<>();
//		this.mixerFuel = FluidIngredient.EMPTY;
	}

	@Override
	public BlazingStandardRecipeBuilder<R> require(FluidIngredient fluidIngredient) {
		return (BlazingStandardRecipeBuilder<R>) super.require(fluidIngredient);
	}

	@Override
	public BlazingStandardRecipeBuilder<R> output(Fluid fluid, MultiAmount amount) {
		return fluidOutput(self(), fluid, amount);
	}

	@Override
	public BlazingStandardRecipeBuilder<R> requireMultiple(Ingredient ingredient, int amount) {
		for (int i = 0; i < amount; i++) {
			require(ingredient);
		}
		return self();
	}

	@Override
	public BlazingStandardRecipeBuilder<R> withConditions(List<LoadCondition<?>> conditions) {
		loadConditions.addAll(conditions);
		return self();
	}

	@Override
	public List<LoadCondition<?>> getLoadConditions() {
		return loadConditions;
	}

	@Override
	public BlazingStandardRecipeBuilder<R> require(Ingredient ingredient) {
		return (BlazingStandardRecipeBuilder<R>) super.require(ingredient);
	}

	@Override
	public BlazingStandardRecipeBuilder<R> self() {
		return this;
	}

	@Override
	public ResourceLocation getRecipeId() {
		return recipeId;
	}

	@Override
	public void build(RecipeOutput consumer) {
//		require(mixerFuel);
		IBlazingRecipeBuilder.super.build(consumer);
	}

//	public BlazingStandardRecipeBuilder<R> mixerFuel(FluidIngredient mixerFuel) {
//		this.mixerFuel = mixerFuel;
//		return self();
//	}
//
//	public BlazingStandardRecipeBuilder<R> mixerFuel(Fluid fluid, MultiAmount amount) {
//		return mixerFuel(MultiFluidIngredient.fromFluid(BuiltInRegistries.FLUID.wrapAsHolder(fluid), amount));
//	}
//
//	public BlazingStandardRecipeBuilder<R> mixerFuel(TagKey<Fluid> fluidTag, MultiAmount amount) {
//		return mixerFuel(MultiFluidIngredient.fromTag(fluidTag, amount));
//	}

	@ExpectPlatform
	public static <R extends StandardProcessingRecipe<?>, S extends StandardProcessingRecipe.Builder<R>> S fluidOutput(S builder, Fluid fluid, MultiAmount amount) {
		throw new AssertionError();
	}
}
