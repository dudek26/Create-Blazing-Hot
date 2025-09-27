package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidIngredient;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeMixingRecipeBuilder extends BlazingRecipeBuilder<ProcessingRecipeParams, BlazeMixingRecipe, BlazeMixingRecipeBuilder> {

	protected FluidIngredient mixerFuel;

	public BlazeMixingRecipeBuilder(ProcessingRecipe.Factory<ProcessingRecipeParams, BlazeMixingRecipe> factory, ResourceLocation recipeId) {
		super(factory, recipeId);
		mixerFuel = FluidIngredient.EMPTY;
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
	public BlazeMixingRecipe build() {
		require(mixerFuel); // temporary solution to params issue
		return factory.create(createParams());
	}

	public BlazeMixingRecipeBuilder mixerFuel(FluidIngredient mixerFuel) {
		this.mixerFuel = mixerFuel;
		return self();
	}

	public BlazeMixingRecipeBuilder mixerFuel(Fluid fluid, MultiAmount amount) {
		return mixerFuel(MultiFluidIngredient.fromFluid(BuiltInRegistries.FLUID.wrapAsHolder(fluid), amount));
	}

	public BlazeMixingRecipeBuilder mixerFuel(TagKey<Fluid> fluidTag, MultiAmount amount) {
		return mixerFuel(MultiFluidIngredient.fromTag(fluidTag, amount));
	}

}
