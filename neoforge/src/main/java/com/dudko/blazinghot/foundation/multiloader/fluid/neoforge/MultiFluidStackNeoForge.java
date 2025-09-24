package com.dudko.blazinghot.foundation.multiloader.fluid.neoforge;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidStack;

import net.neoforged.neoforge.fluids.FluidStack;

public class MultiFluidStackNeoForge {

	public static MultiFluidStack fromNeoForgeStack(FluidStack stack) {
		return new MultiFluidStack(stack.getFluidHolder(), MultiAmount.from(stack.getAmount()), stack.getComponents());
	}

	public static FluidStack toNeoForgeStack(MultiFluidStack stack) {
		return new FluidStack(stack.fluid(), stack.amount().getInt(), stack.components().asPatch());
	}

}
