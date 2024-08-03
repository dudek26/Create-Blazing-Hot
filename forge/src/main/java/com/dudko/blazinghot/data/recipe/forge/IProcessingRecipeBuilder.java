package com.dudko.blazinghot.data.recipe.forge;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IProcessingRecipeBuilder<T extends ProcessingRecipe<?>> {

    default ProcessingRecipeBuilder<T> blazinghot$requireFuel(TagKey<Fluid> fluidTag, int amount) {
        return blazinghot$requireFuel(FluidIngredient.fromTag(fluidTag, amount));
    }

    ProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidIngredient ingredient);

    default ProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidStack fluidStack) {
        return blazinghot$requireFuel(FluidIngredient.fromFluidStack(fluidStack));
    }

}
