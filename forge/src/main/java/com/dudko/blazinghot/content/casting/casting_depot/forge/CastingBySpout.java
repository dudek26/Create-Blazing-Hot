package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.List;

import javax.annotation.Nullable;

import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CastingBySpout {

	private static final RecipeWrapper WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

	public static boolean canItemBeCast(Level world, ItemStack stack) {
		WRAPPER.setItem(0, stack);
		return BlazingRecipeTypes.CASTING.find(WRAPPER, world).isPresent();
	}

	public static int getRequiredAmountForItem(Level world, ItemStack stack, FluidStack availableFluid) {
		WRAPPER.setItem(0, stack);

		for (Recipe<RecipeWrapper> recipe : world
				.getRecipeManager()
				.getRecipesFor(BlazingRecipeTypes.CASTING.getType(), WRAPPER, world)) {
			CastingRecipe castingRecipe = (CastingRecipe) recipe;
			FluidIngredient requiredFluid = castingRecipe.getRequiredFluid();
			if (requiredFluid.test(availableFluid)) return requiredFluid.getRequiredAmount();
		}
		return -1;
	}

	public static CastingRecipe findRecipe(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid) {
		FluidStack toCast = availableFluid.copy();
		toCast.setAmount(requiredAmount);

		WRAPPER.setItem(0, stack);

		CastingRecipe castingRecipe = null;
		for (Recipe<RecipeWrapper> recipe : world
				.getRecipeManager()
				.getRecipesFor(BlazingRecipeTypes.CASTING.getType(), WRAPPER, world)) {
			CastingRecipe cr = (CastingRecipe) recipe;
			FluidIngredient requiredFluid = cr.getRequiredFluid();
			if (requiredFluid.test(toCast)) castingRecipe = cr;
		}
		return castingRecipe;
	}

	public static ItemStack getCastingResult(@Nullable CastingRecipe recipe) {
		if (recipe == null) return null;
		List<ItemStack> results = recipe.rollResults();
		return results.isEmpty() ? ItemStack.EMPTY : results.get(0);
	}

	public static void finishCasting(CastingRecipe recipe, ItemStack stack, FluidStack availableFluid) {
		availableFluid.shrink(recipe.getRequiredFluid().getRequiredAmount());
		// TODO: add mold consuming
		stack.shrink(1);
	}
}
