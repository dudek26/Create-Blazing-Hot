package com.dudko.blazinghot.foundation.multiloader.fluid.neoforge;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidStack;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

/**
 * @see com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidIngredient
 */
public class MultiFluidIngredientImpl {

	public static FluidIngredient fromStack(MultiFluidStack fluidStack) {
		return FluidIngredient.fromFluidStack(MultiFluidStackNeoForge.toNeoForgeStack(fluidStack));
	}

	public static FluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount) {
		return FluidIngredient.fromTag(tag, (int) amount.get());
	}

}
