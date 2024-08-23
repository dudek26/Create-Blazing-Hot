package com.dudko.blazinghot.mixin.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.foundation.fluid.FluidIngredient;

@Mixin(FluidIngredient.class)
public interface FluidIngredientAccessor {

	@Accessor(remap = false)
	void setAmountRequired(int requiredAmount);

}
