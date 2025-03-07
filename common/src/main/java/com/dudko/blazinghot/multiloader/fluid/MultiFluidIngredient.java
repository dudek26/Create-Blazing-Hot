package com.dudko.blazinghot.multiloader.fluid;

import com.simibubi.create.foundation.fluid.FluidIngredient;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluidIngredient {

	@ExpectPlatform
	public static FluidIngredient fromStack(MultiFluidStack fluidStack) {
		throw new AssertionError();
	}

	public static FluidIngredient fromFluid(Fluid fluid, MultiAmount amount) {
		return fromStack(new MultiFluidStack(fluid, amount));
	}

	@ExpectPlatform
	public static FluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount) {
		throw new AssertionError();
	}
}
