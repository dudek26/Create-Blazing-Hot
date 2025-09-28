package com.dudko.blazinghot.foundation.multiloader.fluid;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public record MultiFluidStack(Holder<Fluid> fluid, MultiAmount amount, DataComponentPatch components) {

	public static final MultiFluidStack
			EMPTY =
			new MultiFluidStack(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.EMPTY), MultiAmount.EMPTY);

	public MultiFluidStack(Holder<Fluid> fluid, MultiAmount amount) {
		this(fluid, amount, DataComponentPatch.EMPTY);
	}

}
