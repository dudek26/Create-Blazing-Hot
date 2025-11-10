package com.dudko.blazinghot.multiloader.fluid;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluids {

	public static final float MELTABLE_CONVERSION_LEGACY = 9000 / 144f;
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
	public static boolean recipeResultContains(ProcessingRecipe<?> r, TagKey<Fluid> fluid) {
		return true;
	}

	@ExpectPlatform
	public static FluidIngredient fluidIngredientFromFluid(Fluid fluid, long amount) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static long getFluidAmount(FluidIngredient ingredient) {
		return 1;
	}
}
