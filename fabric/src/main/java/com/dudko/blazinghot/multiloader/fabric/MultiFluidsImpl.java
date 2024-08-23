package com.dudko.blazinghot.multiloader.fabric;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluidsImpl {
	public static long platformedAmount(long droplets) {
		return droplets;
	}

	public static String platformedName() {
		return "droplets";
	}

	public static String conversionNote() {
		return "(1 milibucket = 81 droplets)";
	}

	public static boolean recipeResultContains(ProcessingRecipe<?> r, TagKey<Fluid> fluid) {
		return r.getFluidResults().stream().anyMatch(fs -> fs.getFluid().defaultFluidState().is(fluid));
	}
}
