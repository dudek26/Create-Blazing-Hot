package com.dudko.blazinghot.foundation.multiloader.fluid.neoforge;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidStack;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

/**
 * @see com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidIngredient
 */
public class MultiFluidIngredientImpl {

	public static SizedFluidIngredient fromStack(MultiFluidStack fluidStack) {
		return SizedFluidIngredient.of(MultiFluidStackNeoForge.toNeoForgeStack(fluidStack));
	}

	public static SizedFluidIngredient fromTag(TagKey<Fluid> tag, MultiAmount amount) {
		return SizedFluidIngredient.of(tag, (int) amount.get());
	}

}
