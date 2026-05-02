package com.dudko.blazinghot.compat.jei.category.neoforge;

import com.dudko.blazinghot.compat.jei.category.BlazeMixingCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;

public abstract class BlazeMixingCategoryImpl extends BlazeMixingCategory {

	protected BlazeMixingCategoryImpl(Info<BasinRecipe> info, MixingType type) {
		super(info, type);
	}

	public static int getFluidResultsSize(BasinRecipe recipe) {
		return recipe.getFluidResults().size();
	}

}
