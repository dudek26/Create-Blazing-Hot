package com.dudko.blazinghot.compat.jei;


import dev.architectury.injectables.annotations.ExpectPlatform;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class BlazingJEIHelper {

	@ExpectPlatform
	public static IRecipeSlotBuilder addFluidSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, SizedFluidIngredient ingredient) {
		throw new AssertionError();
	}
}
