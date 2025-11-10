package com.dudko.blazinghot.multiloader.fluid.forge;

import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidStack;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluidIngredientImpl {

	public static FluidIngredient fromStack(MultiFluidStack fluidStack) {
		return FluidIngredient.fromFluid(fluidStack.getFluid(),
				(int) fluidStack.getAmount().get(fluidStack.isLegacy()));
	}

	public static FluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount, boolean legacy) {
		return FluidIngredient.fromTag(tag, (int) amount.get(legacy));
	}

}
