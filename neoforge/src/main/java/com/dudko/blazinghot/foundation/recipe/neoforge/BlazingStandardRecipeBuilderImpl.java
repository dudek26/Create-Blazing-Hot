package com.dudko.blazinghot.foundation.recipe.neoforge;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class BlazingStandardRecipeBuilderImpl {

	public static <R extends StandardProcessingRecipe<?>, S extends StandardProcessingRecipe.Builder<R>> S fluidOutput(S builder, Fluid fluid, MultiAmount amount) {
		return (S) builder.output(new FluidStack(fluid, amount.millibuckets()));
	}

}
