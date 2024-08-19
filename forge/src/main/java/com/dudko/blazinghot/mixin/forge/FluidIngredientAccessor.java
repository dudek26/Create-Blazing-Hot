package com.dudko.blazinghot.mixin.forge;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidIngredient.class)
public interface FluidIngredientAccessor {

    @Accessor(remap=false)
    void setAmountRequired(int requiredAmount);

}
