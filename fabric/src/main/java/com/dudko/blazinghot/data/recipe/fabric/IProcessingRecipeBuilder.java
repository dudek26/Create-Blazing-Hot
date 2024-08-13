package com.dudko.blazinghot.data.recipe.fabric;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public interface IProcessingRecipeBuilder<T extends ProcessingRecipe<?>> {

    default IProcessingRecipeBuilder<T> blazinghot$requireFuel(TagKey<Fluid> fluidTag, long amount) {
        return blazinghot$requireFuel(FluidIngredient.fromTag(fluidTag, amount));
    }

    IProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidIngredient ingredient);

    default IProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidStack fluidStack) {
        return blazinghot$requireFuel(FluidIngredient.fromFluidStack(fluidStack));
    }

    IProcessingRecipeBuilder<T> blazinghot$convertFluidAmounts(boolean bool);

    default ProcessingRecipeBuilder<T> finish() {
        return (ProcessingRecipeBuilder<T>) this;
    }

}
