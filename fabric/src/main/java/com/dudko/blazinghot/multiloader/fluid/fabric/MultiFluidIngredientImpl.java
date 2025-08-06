package com.dudko.blazinghot.multiloader.fluid.fabric;

import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidStack;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluidIngredientImpl {

	public static FluidIngredient fromStack(MultiFluidStack fluidStack) {
		return FluidIngredient.fromFluid(fluidStack.getFluid(), fluidStack.getAmount().get());
	}

	public static FluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount) {
		return FluidIngredient.fromTag(tag, amount.get());
	}

}
