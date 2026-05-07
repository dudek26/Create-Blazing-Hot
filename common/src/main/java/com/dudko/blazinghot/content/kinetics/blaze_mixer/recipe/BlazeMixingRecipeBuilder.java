package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.foundation.recipe.BlazingRecipeBuilder;
import com.dudko.blazinghot.registry.BlazingTags.Fluids;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeMixingRecipeBuilder extends BlazingRecipeBuilder<ProcessingRecipeParams, BlazeMixingRecipe, BlazeMixingRecipeBuilder> {

	protected long fuelAmount;

	public BlazeMixingRecipeBuilder(ProcessingRecipe.Factory<ProcessingRecipeParams, BlazeMixingRecipe> factory, ResourceLocation recipeId) {
		super(factory, recipeId);
		fuelAmount = 0;
	}

	@Override
	protected BlazeMixingRecipeParams createParams() {
		return new BlazeMixingRecipeParams();
	}

	@Override
	public BlazeMixingRecipeBuilder self() {
		return this;
	}

	@Override
	public void build(RecipeOutput consumer) {
		if (fuelAmount > 0) require(SizedFluidIngredient.of(Fluids.BLAZE_MIXER_FUEL.tag(), (int) fuelAmount));
		super.build(consumer);
	}

	public BlazeMixingRecipeBuilder mixerFuel(long fuelAmount) {
		this.fuelAmount = fuelAmount;
		return self();
	}

}
