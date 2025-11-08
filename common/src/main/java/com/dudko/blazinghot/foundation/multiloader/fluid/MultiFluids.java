package com.dudko.blazinghot.foundation.multiloader.fluid;

import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class MultiFluids {

	/**
	 * DROPLETS to MILLIBUCKETS ratio for ingots
	 */
	public static final float MELTABLE_CONVERSION = 9000 / 90f;

	@ExpectPlatform
	public static String platformedName() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static String conversionNote() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean recipeResultContains(StandardProcessingRecipe<?> r, TagKey<Fluid> fluid) {
		return true;
	}

	@ExpectPlatform
	public static SizedFluidIngredient fluidIngredientFromFluid(Fluid fluid, long amount) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static long getFluidAmount(SizedFluidIngredient ingredient) {
		return 1;
	}
}
