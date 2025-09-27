package com.dudko.blazinghot.compat.jei.category.neoforge;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.compat.jei.category.BlazeMixingCategory;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.neoforged.neoforge.fluids.FluidStack;

public abstract class BlazeMixingCategoryImpl extends BlazeMixingCategory {

	protected BlazeMixingCategoryImpl(Info<BasinRecipe> info, MixingType type) {
		super(info, type);
	}

	public static boolean includeFuel(BlazeMixingCategory.MixingType type, BasinRecipe recipe) {
		FluidIngredient fuelFluid = getFuelFromRecipe(type, recipe);

		List<FluidStack> fuels;
		if (fuelFluid == FluidIngredient.EMPTY) fuels = new ArrayList<>();
		else fuels = new ArrayList<>(fuelFluid.getMatchingFluidStacks());
		return (!fuels.isEmpty() && !fuels.getFirst().isEmpty() && fuels.getFirst() != null) || (type
				== BlazeMixingCategory.MixingType.AUTO_SHAPELESS
				&& BlazingConfigs.server().recipes.blazeShapelessFuelUsage.get() != 0);
	}

	public static int getFluidResultsSize(BasinRecipe recipe) {
		return recipe.getFluidResults().size();
	}

}
