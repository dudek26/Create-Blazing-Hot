package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.andesiteCasing;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.blazeGoldSheet;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.copperCasing;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.sturdyAlloy;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class ItemApplicationRecipeGen extends com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen {

	public ItemApplicationRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	GeneratedRecipe
			BLAZE_CASING =
			create("blaze_casing_from_copper",
					b -> b.require(copperCasing()).require(blazeGoldSheet()).output(BlazingBlocks.BLAZE_CASING)),
			STURDY_CASING =
					create("sturdy_casing_from_andesite",
							b -> b
									.require(andesiteCasing())
									.require(sturdyAlloy())
									.output(BlazingBlocks.STURDY_CASING));

}
