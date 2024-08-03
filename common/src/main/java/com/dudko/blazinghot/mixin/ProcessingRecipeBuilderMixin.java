package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.data.recipe.BlazeMixingRecipeBuilder;
import com.dudko.blazinghot.data.recipe.IProcessingRecipeBuilder;
import com.dudko.blazinghot.data.recipe.IProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProcessingRecipeBuilder.class)
public abstract class ProcessingRecipeBuilderMixin<T extends ProcessingRecipe<?>> implements IProcessingRecipeBuilder<T> {

    @Shadow
    protected ProcessingRecipeBuilder.ProcessingRecipeParams params;

    @SuppressWarnings("unchecked")
    @Override
    public ProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidIngredient fuel) {
        ((IProcessingRecipeParams) params).blazinghot$setFuelFluid(fuel);
        return (ProcessingRecipeBuilder<T>) (Object) this;
    }

}
