package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.neoforge;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidIngredient;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeMixingRecipeImpl extends BlazeMixingRecipe {

	protected BlazeMixingRecipeImpl(ProcessingRecipeParams params) {
		super(params);
	}

	@Override
	public NonNullList<SizedFluidIngredient> getFluidIngredients() {
		NonNullList<SizedFluidIngredient> fluidIngredients = NonNullList.create();
		fluidIngredients.addAll(super.getFluidIngredients());
		fluidIngredients.removeLast();
		return fluidIngredients;
	}

	public static BlazeMixingRecipe create(ProcessingRecipeParams params) {
		return new BlazeMixingRecipeImpl(params);
	}

	@Override
	public int getMixerFuelAmount() {
		if (super.getFluidIngredients().isEmpty()) {
			return 0;
		}
		SizedFluidIngredient fuel = super.getFluidIngredients()
			.stream()
			.filter(BlazeMixingRecipeImpl::isPlaceholder)
			.findFirst()
			.orElse(MultiFluidIngredient.empty());
		if (fuel.ingredient().isEmpty()) return 0;
		return fuel.amount();
	}

	public static boolean isPlaceholder(SizedFluidIngredient fluidIngredient) {
		return fluidIngredient.getFluids().length == 0;
	}

}
