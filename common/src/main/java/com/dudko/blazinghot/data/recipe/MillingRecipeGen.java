package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.cinderFlour;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.soulSand;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.stone;

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
					b -> b
							.require(soulSand())
							.output(BlazingItems.SOUL_DUST)
							.averageProcessingDuration()
							.output(0.5F, BlazingItems.SOUL_DUST)),
			STONE =
					create("stone",
							b -> b
									.require(stone())
									.output(BlazingItems.STONE_DUST)
									.averageProcessingDuration()
									.output(0.5F, BlazingItems.STONE_DUST)),
			CINDER_FLOUR =
					create("cinder_flour",
							b -> b
									.require(cinderFlour())
									.output(0.75f, BlazingItems.NETHERRACK_DUST)
									.averageProcessingDuration());

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MILLING;
	}


}
