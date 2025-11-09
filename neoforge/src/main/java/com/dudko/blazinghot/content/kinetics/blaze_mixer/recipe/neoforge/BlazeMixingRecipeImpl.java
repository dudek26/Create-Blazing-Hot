package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.neoforge;

import com.simibubi.create.content.processing.basin.BasinRecipe;

import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class BlazeMixingRecipeImpl {

	public static boolean isMeltingRecipe(Recipe<?> recipe) {
		return recipe instanceof BasinRecipe basinRecipe && basinRecipe.getIngredients().size() == 1 && basinRecipe
				.getFluidIngredients()
				.isEmpty() && !basinRecipe.getFluidResults().isEmpty() && basinRecipe.getRollableResults().isEmpty();
	}

	public static boolean isPlaceholder(SizedFluidIngredient fluidIngredient) {
		return fluidIngredient.getFluids().length == 0;
	}

}
