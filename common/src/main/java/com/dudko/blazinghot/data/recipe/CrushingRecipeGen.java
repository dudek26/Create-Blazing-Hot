package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.blazeGoldRod;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.cinderFlour;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.soulSand;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stone;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

public class CrushingRecipeGen extends com.simibubi.create.api.data.recipe.CrushingRecipeGen {

	public CrushingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	GeneratedRecipe
			STONE =
			create("stone",
					b -> b
							.require(stone())
							.output(BlazingItems.STONE_DUST, 2)
							.output(0.6F, BlazingItems.STONE_DUST)
							.output(0.4F, BlazingItems.STONE_DUST)
							.duration(250)),
			SOUL_SAND =
					create("soul_sand",
							b -> b
									.require(soulSand())
									.output(BlazingItems.SOUL_DUST, 2)
									.output(0.6F, BlazingItems.SOUL_DUST)
									.output(0.4F, BlazingItems.SOUL_DUST)
									.duration(200)),
			BLAZE_GOLD_ROD =
					create("blaze_gold_rod",
							b -> b.require(blazeGoldRod()).output(0.5F, Items.BLAZE_POWDER).duration(250)),
			CINDER_FLOUR =
					create("cinder_flour",
							b -> b.require(cinderFlour()).output(BlazingItems.NETHERRACK_DUST).duration(150));

}
