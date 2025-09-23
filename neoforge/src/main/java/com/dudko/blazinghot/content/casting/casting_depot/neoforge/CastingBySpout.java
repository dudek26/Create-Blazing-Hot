package com.dudko.blazinghot.content.casting.casting_depot.neoforge;

import java.util.List;

import javax.annotation.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

// TODO: pull some of the methods up
public class CastingBySpout {

	public static boolean canItemBeCast(Level world, ItemStack stack) {
		SingleRecipeInput input = new SingleRecipeInput(stack);
		return BlazingRecipeTypes.CASTING.find(input, world).isPresent();
	}

	public static int getRequiredAmountForItem(Level world, ItemStack stack, FluidStack availableFluid) {
		SingleRecipeInput input = new SingleRecipeInput(stack);

		for (RecipeHolder<Recipe<SingleRecipeInput>> recipe : world
				.getRecipeManager()
				.getRecipesFor(BlazingRecipeTypes.CASTING.getType(), input, world)) {
			CastingRecipe castingRecipe = (CastingRecipe) recipe.value();
			FluidIngredient requiredFluid = castingRecipe.getRequiredFluid();
			if (requiredFluid.test(availableFluid)) return requiredFluid.getRequiredAmount();
		}
		return -1;
	}

	@SuppressWarnings("DataFlowIssue")
	@Nullable
	public static CastingRecipe findRecipe(CastingDepotBlockEntity depot, Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid) {
		SingleRecipeInput input = new SingleRecipeInput(stack);

		FluidStack toCast = availableFluid.copy();
		toCast.setAmount(requiredAmount);

		CastingRecipe castingRecipe = null;
		for (RecipeHolder<Recipe<SingleRecipeInput>> recipe : world
				.getRecipeManager()
				.getRecipesFor(BlazingRecipeTypes.CASTING.getType(), input, world)) {
			CastingRecipe cr = (CastingRecipe) recipe.value();
			FluidIngredient requiredFluid = cr.getRequiredFluid();
			if (requiredFluid.test(toCast)) castingRecipe = cr;
		}

		if (castingRecipe == null) return null;
		return matchFilter(depot, castingRecipe) ? castingRecipe : null;
	}

	@Nullable
	public static CastingRecipe findRecipe(CastingDepotBlockEntity depot, Level world, ResourceLocation id) {
		RecipeHolder<?> holder = world.getRecipeManager().byKey(id).orElse(null);
		if (holder == null) return null;
		if (!(holder.value() instanceof CastingRecipe castingRecipe)) return null;
		return matchFilter(depot, castingRecipe) ? castingRecipe : null;
	}

	public static ItemStack getCastingResult(@Nullable CastingRecipe recipe) {
		if (recipe == null) return null;
		List<ItemStack> results = recipe.rollResults();
		return results.isEmpty() ? ItemStack.EMPTY : results.getFirst();
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
