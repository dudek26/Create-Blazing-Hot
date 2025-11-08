package com.dudko.blazinghot.foundation.multiloader.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class MultiFluidIngredient {

	@ExpectPlatform
	public static SizedFluidIngredient fromStack(MultiFluidStack fluidStack) {
		throw new AssertionError();
	}

	public static SizedFluidIngredient fromFluid(Holder<Fluid> fluid, MultiAmount amount) {
		return fromStack(new MultiFluidStack(fluid, amount));
	}

	public static SizedFluidIngredient fromFluid(Holder<Fluid> fluid, MultiAmount amount, DataComponentPatch components) {
		return fromStack(new MultiFluidStack(fluid, amount, components));
	}

	@ExpectPlatform
	public static SizedFluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount) {
		throw new AssertionError();
	}

	public static SizedFluidIngredient empty() {
		return new SizedFluidIngredient(FluidIngredient.empty(), 1000);
	}
}
