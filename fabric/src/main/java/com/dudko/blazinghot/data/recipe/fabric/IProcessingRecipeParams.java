package com.dudko.blazinghot.data.recipe.fabric;

import com.simibubi.create.foundation.fluid.FluidIngredient;

public interface IProcessingRecipeParams {

    FluidIngredient blazinghot$getFuelFluid();
    boolean blazinghot$formConversion();

    void blazinghot$setFuelFluid(FluidIngredient fuelFluid);
    void blazinghot$setFormConversion(boolean formConversion);

}
