package com.dudko.blazinghot.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeHelper {

	public static boolean doItemIngredientsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
		if (recipe1.getIngredients().isEmpty() && recipe2.getIngredients().isEmpty()) {
			return true;
		}

		ItemStack[] matchingStacks = recipe1.getIngredients().getFirst().getItems();
		if (matchingStacks.length == 0) {
			return false;
		}
		return recipe2.getIngredients().getFirst().test(matchingStacks[0]);
	}

}
