package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.function.UnaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.MixerFuel.Builder;
import com.dudko.blazinghot.foundation.mixin_interfaces.IProcessingRecipeParams;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeMixingRecipeBuilder extends BlazingRecipeBuilder<ProcessingRecipeParams, BlazeMixingRecipe, BlazeMixingRecipeBuilder> {

	public BlazeMixingRecipeBuilder(ProcessingRecipe.Factory<ProcessingRecipeParams, BlazeMixingRecipe> factory,
									ResourceLocation recipeId) {
		super(factory, recipeId);
	}

	@Override
	protected ProcessingRecipeParams createParams() {
		return new BlazeMixingRecipeParams();
	}

	@Override
	public BlazeMixingRecipeBuilder self() {
		return this;
	}


	public BlazeMixingRecipeBuilder mixerFuel(long fuelAmount) {
		return mixerFuel(MixerFuel.simple(fuelAmount));
	}

	public BlazeMixingRecipeBuilder mixerFuel(MixerFuel fuel) {
		IProcessingRecipeParams.cast(params).blazinghot$setMixerFuel(fuel);
		return self();
	}

	public BlazeMixingRecipeBuilder mixerFuel(UnaryOperator<MixerFuel.Builder> builder) {
		return mixerFuel(builder.apply(new Builder()).build());
	}

}
