package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.soulSand;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.stone;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class MillingRecipeGen extends BlazingProcessingRecipeGen {

	public MillingRecipeGen(PackOutput output) {
		super(output);
	}

	GeneratedRecipe
			SOUL_SAND =
			create("soul_sand",
					b -> b.require(soulSand()).output(BlazingItems.SOUL_DUST).output(0.5F, BlazingItems.SOUL_DUST)),
			STONE =
					create("stone",
							b -> b
									.require(stone())
									.output(BlazingItems.STONE_DUST)
									.output(0.5F, BlazingItems.STONE_DUST));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MILLING;
	}


}
