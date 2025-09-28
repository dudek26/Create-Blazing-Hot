package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.cinderFlour;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.soulSand;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stone;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class MillingRecipeGen extends com.simibubi.create.api.data.recipe.MillingRecipeGen {

	public MillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
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
}
