package com.dudko.blazinghot.foundation.multiloader.fluid;

import com.simibubi.create.foundation.fluid.FluidIngredient;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluidIngredient {

	@ExpectPlatform
	public static FluidIngredient fromStack(MultiFluidStack fluidStack) {
		throw new AssertionError();
	}

	public static FluidIngredient fromFluid(Holder<Fluid> fluid, MultiAmount amount) {
		return fromStack(new MultiFluidStack(fluid, amount));
	}

	public static FluidIngredient fromFluid(Holder<Fluid> fluid, MultiAmount amount, DataComponentPatch components) {
		return fromStack(new MultiFluidStack(fluid, amount, components));
	}

	@ExpectPlatform
	public static FluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount) {
		throw new AssertionError();
	}
}
