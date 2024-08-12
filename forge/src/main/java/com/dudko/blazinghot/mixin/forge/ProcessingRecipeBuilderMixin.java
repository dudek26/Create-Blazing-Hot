package com.dudko.blazinghot.mixin.forge;

import com.dudko.blazinghot.data.recipe.forge.IProcessingRecipeBuilder;
import com.dudko.blazinghot.data.recipe.forge.IProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ProcessingRecipeBuilder.class, remap = false)
public class ProcessingRecipeBuilderMixin<T extends ProcessingRecipe<?>> implements IProcessingRecipeBuilder<T> {

    @Shadow
    protected ProcessingRecipeBuilder.ProcessingRecipeParams params;

    @SuppressWarnings("unchecked")
    @Override
    public ProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidIngredient fuel) {
        ((IProcessingRecipeParams) params).blazinghot$setFuelFluid(fuel);
        return (ProcessingRecipeBuilder<T>) (Object) this;
    }

}
