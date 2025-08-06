package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.List;

import javax.annotation.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

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

	@SuppressWarnings("DataFlowIssue")
	@Nullable
	public static CastingRecipe findRecipe(CastingDepotBlockEntity depot, Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid) {
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

		if (castingRecipe == null) return null;
		return matchFilter(depot, castingRecipe) ? castingRecipe : null;
	}

	@Nullable
	public static CastingRecipe findRecipe(CastingDepotBlockEntity depot, Level world, ResourceLocation id) {
		Recipe<?> recipe = world.getRecipeManager().byKey(id).orElse(null);
		if (!(recipe instanceof CastingRecipe castingRecipe)) return null;
		return matchFilter(depot, castingRecipe) ? castingRecipe : null;

	}

	public static ItemStack getCastingResult(@Nullable CastingRecipe recipe) {
		if (recipe == null) return null;
		List<ItemStack> results = recipe.rollResults();
		return results.isEmpty() ? ItemStack.EMPTY : results.get(0);
	}

	public static void finishCasting(boolean keepMold, ItemStack stack) {
		if (keepMold) return;
		stack.shrink(1);
	}

	public static boolean matchFilter(CastingDepotBlockEntity depot, CastingRecipe recipe) {
		FilteringBehaviour filter = depot.getFilter();
		if (filter == null || depot.getLevel() == null) return false;

		boolean filterTest = filter.test(recipe.getResultItem(depot.getLevel().registryAccess()));

		if (recipe.getRollableResults().isEmpty() && !recipe.getFluidResults().isEmpty())
			filterTest = filter.test(recipe.getFluidResults().get(0));


		return filterTest;
	}
}
