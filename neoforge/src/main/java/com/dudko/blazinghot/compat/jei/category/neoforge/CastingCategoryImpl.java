package com.dudko.blazinghot.compat.jei.category.neoforge;

import java.util.Arrays;
import java.util.List;

import com.dudko.blazinghot.compat.jei.category.CastingCategory;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;

import net.minecraft.world.level.material.FluidState;

public abstract class CastingCategoryImpl extends CastingCategory {

	public CastingCategoryImpl(Info<CastingRecipe> info) {
		super(info);
	}

	public static List<FluidState> getRequiredFluids(CastingRecipe recipe) {
		return Arrays
				.stream(recipe.getRequiredFluid().getFluids())
				.map(stack -> stack.getFluid().defaultFluidState())
				.toList();
	}

}
