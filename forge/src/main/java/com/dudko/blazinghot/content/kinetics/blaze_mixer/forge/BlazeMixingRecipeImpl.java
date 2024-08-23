package com.dudko.blazinghot.content.kinetics.blaze_mixer.forge;

import com.dudko.blazinghot.mixin.forge.FluidIngredientAccessor;
import com.simibubi.create.foundation.fluid.FluidIngredient;

public class BlazeMixingRecipeImpl {
	public static void platformFuel(FluidIngredient fuel) {
		((FluidIngredientAccessor) fuel).setAmountRequired(fuel.getRequiredAmount() / 81);
	}
}
