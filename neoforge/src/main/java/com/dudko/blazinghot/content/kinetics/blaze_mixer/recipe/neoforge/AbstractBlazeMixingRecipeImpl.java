package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.neoforge;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class AbstractBlazeMixingRecipeImpl {
	
	public static boolean isPlaceholder(SizedFluidIngredient fluidIngredient) {
		return fluidIngredient.getFluids().length == 0;
	}

}
