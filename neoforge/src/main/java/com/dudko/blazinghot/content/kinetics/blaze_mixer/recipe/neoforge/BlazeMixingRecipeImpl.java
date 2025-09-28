package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.neoforge;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.world.item.crafting.Recipe;

public class BlazeMixingRecipeImpl {

	public static boolean isMeltingRecipe(Recipe<?> recipe) {
		return recipe instanceof BasinRecipe basinRecipe && basinRecipe.getIngredients().size() == 1 && basinRecipe
				.getFluidIngredients()
				.isEmpty() && !basinRecipe.getFluidResults().isEmpty() && basinRecipe.getRollableResults().isEmpty();
	}

	public static boolean isPlaceholder(FluidIngredient fluidIngredient) {
		return fluidIngredient.test(BlazeMixingRecipe.emptyMixerFuel().getMatchingFluidStacks().getFirst());
	}

}
