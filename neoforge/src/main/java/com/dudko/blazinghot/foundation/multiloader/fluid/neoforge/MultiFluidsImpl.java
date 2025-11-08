package com.dudko.blazinghot.foundation.multiloader.fluid.neoforge;

import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class MultiFluidsImpl {
	public static long platformedAmount(long droplets) {
		return droplets / 81;
	}

	public static String platformedName() {
		return "milibuckets";
	}

	public static String conversionNote() {
		return "";
	}

	public static boolean recipeResultContains(StandardProcessingRecipe<?> r, TagKey<Fluid> fluid) {
		return r.getFluidResults().stream().anyMatch(fs -> fs.getFluid().defaultFluidState().is(fluid));
	}

	public static SizedFluidIngredient fluidIngredientFromFluid(Fluid fluid, long amount) {
		return SizedFluidIngredient.of(fluid, (int) platformedAmount(amount));
	}

	public static long getFluidAmount(SizedFluidIngredient ingredient) {
		return ingredient.amount();
	}
}
