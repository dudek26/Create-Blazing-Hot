package com.dudko.blazinghot.compat.jei;

import com.simibubi.create.foundation.fluid.FluidIngredient;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;

public class BlazingJEIHelper {

	@ExpectPlatform
	public static IRecipeSlotBuilder addFluidSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, FluidIngredient ingredient) {
		throw new AssertionError();
	}
}
