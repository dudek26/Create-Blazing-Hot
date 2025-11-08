package com.dudko.blazinghot.content.casting.casting_depot;

import java.util.List;

import javax.annotation.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidStack;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class CastingBySpout {
	public static boolean canItemBeCast(Level world, ItemStack stack) {
		SingleRecipeInput input = new SingleRecipeInput(stack);
		return BlazingRecipeTypes.CASTING.find(input, world).isPresent();
	}

	@ExpectPlatform
	public static int getRequiredAmountForItem(Level world, ItemStack stack, MultiFluidStack availableFluid) {
		throw new AssertionError();
	}

	@Nullable
	@ExpectPlatform
	public static RecipeHolder<CastingRecipe> findRecipe(CastingDepotBlockEntity depot, Level world, int requiredAmount, ItemStack stack, MultiFluidStack availableFluid) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean matchFilter(CastingDepotBlockEntity depot, CastingRecipe recipe) {
		throw new AssertionError();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static RecipeHolder<CastingRecipe> findRecipe(CastingDepotBlockEntity depot, Level world, ResourceLocation id) {
		RecipeHolder<?> holder = world.getRecipeManager().byKey(id).orElse(null);
		if (holder == null) return null;
		if (!(holder.value() instanceof CastingRecipe castingRecipe)) return null;
		return CastingBySpout.matchFilter(depot, castingRecipe) ? (RecipeHolder<CastingRecipe>) holder : null;
	}

	public static ItemStack getCastingResult(@Nullable CastingRecipe recipe, Level level) {
		if (recipe == null) return null;
		List<ItemStack> results = recipe.rollResults(level.getRandom());
		return results.isEmpty() ? ItemStack.EMPTY : results.getFirst();
	}

	public static void finishCasting(boolean keepMold, ItemStack stack) {
		if (keepMold) return;
		stack.shrink(1);
	}
}
