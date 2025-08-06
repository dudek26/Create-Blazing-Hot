package com.dudko.blazinghot.data.lang.fabric;

import com.dudko.blazinghot.BlazingHot;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.world.level.material.Fluid;

public class BlazingLangImpl {

	public static LangBuilder fluidName(Fluid fluid) {
		return BlazingHot.lang().add(new FluidStack(fluid, 1).getDisplayName());
	}
	
}
