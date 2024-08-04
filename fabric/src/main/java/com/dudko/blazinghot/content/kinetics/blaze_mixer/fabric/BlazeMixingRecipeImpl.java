package com.dudko.blazinghot.content.kinetics.blaze_mixer.fabric;

import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;

public class BlazeMixingRecipeImpl {
    public static FluidIngredient getFuelFluid(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        return ((IProcessingRecipeParams) params).blazinghot$getFuelFluid();
    }
}
