package com.dudko.blazinghot.foundation.multiloader.fluid.neoforge;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidStack;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluidIngredientImpl {

	public static FluidIngredient fromStack(MultiFluidStack fluidStack) {
		return FluidIngredient.fromFluid(fluidStack.fluid().value(), (int) fluidStack.amount().get());
	}

	public static FluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount) {
		return FluidIngredient.fromTag(tag, (int) amount.get());
	}

}
