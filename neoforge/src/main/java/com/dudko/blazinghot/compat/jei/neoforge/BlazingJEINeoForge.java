package com.dudko.blazinghot.compat.jei.neoforge;


import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.compat.jei.BlazingJEI;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.registry.neoforge.BlazingFluidsImpl;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;

import net.neoforged.neoforge.fluids.FluidStack;

@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazingJEINeoForge extends BlazingJEI {

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		super.registerRecipes(registration);
		registration.addIngredientInfo((new FluidStack(BlazingFluidsImpl.NETHER_LAVA.get().getSource(), 1000)),
			NeoForgeTypes.FLUID_STACK,
			BlazingLang.NETHER_LAVA_INFO.get());
	}

}
