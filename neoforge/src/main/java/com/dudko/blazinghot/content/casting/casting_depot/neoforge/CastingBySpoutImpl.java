package com.dudko.blazinghot.content.casting.casting_depot.neoforge;

import javax.annotation.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingBySpout;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidStack;
import com.dudko.blazinghot.foundation.multiloader.fluid.neoforge.MultiFluidStackNeoForge;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * @see CastingBySpout
 */
public class CastingBySpoutImpl {

	public static int getRequiredAmountForItem(Level world, ItemStack stack, MultiFluidStack availableFluid) {
		SingleRecipeInput input = new SingleRecipeInput(stack);

		for (RecipeHolder<Recipe<SingleRecipeInput>> recipe : world
				.getRecipeManager()
				.getRecipesFor(BlazingRecipeTypes.CASTING.getType(), input, world)) {
			CastingRecipe castingRecipe = (CastingRecipe) recipe.value();
			FluidIngredient requiredFluid = castingRecipe.getRequiredFluid();
			if (requiredFluid.test(MultiFluidStackNeoForge.toNeoForgeStack(availableFluid)))
				return requiredFluid.getRequiredAmount();
		}
		return -1;
	}

	@SuppressWarnings({"unchecked"})
	@Nullable
	public static RecipeHolder<CastingRecipe> findRecipe(CastingDepotBlockEntity depot, Level world, int requiredAmount, ItemStack stack, MultiFluidStack availableFluid) {
		SingleRecipeInput input = new SingleRecipeInput(stack);

		FluidStack toCast = MultiFluidStackNeoForge.toNeoForgeStack(availableFluid);
		toCast.setAmount(requiredAmount);

		RecipeHolder<CastingRecipe> castingRecipe = null;
		for (RecipeHolder<?> recipe : world
				.getRecipeManager()
				.getRecipesFor(BlazingRecipeTypes.CASTING.getType(), input, world)) {
			RecipeHolder<CastingRecipe> cr = (RecipeHolder<CastingRecipe>) recipe;
			FluidIngredient requiredFluid = cr.value().getRequiredFluid();
			if (requiredFluid.test(toCast)) castingRecipe = cr;
		}

		if (castingRecipe == null) return null;
		return matchFilter(depot, castingRecipe.value()) ? castingRecipe : null;
	}

	public static boolean matchFilter(CastingDepotBlockEntity depot, CastingRecipe recipe) {
		FilteringBehaviour filter = depot.getFilter();
		if (filter == null || depot.getLevel() == null) return false;

		boolean filterTest = filter.test(recipe.getResultItem(depot.getLevel().registryAccess()));

		if (recipe.getRollableResults().isEmpty() && !recipe.getFluidResults().isEmpty())
			filterTest = filter.test(recipe.getFluidResults().getFirst());


		return filterTest;
	}
}
