package com.dudko.blazinghot.data.lang.neoforge;

import com.dudko.blazinghot.BlazingHot;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.world.level.material.Fluid;

import net.neoforged.neoforge.fluids.FluidStack;

/**
 * @see com.dudko.blazinghot.data.lang.BlazingLang
 */
public class BlazingLangImpl {

	public static LangBuilder fluidName(Fluid fluid) {
		return BlazingHot.lang().add(new FluidStack(fluid, 1).getHoverName());
	}
}
