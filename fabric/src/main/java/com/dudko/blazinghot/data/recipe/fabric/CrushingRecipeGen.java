package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.blazeGoldRod;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.soulSand;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.stone;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class CrushingRecipeGen extends BlazingProcessingRecipeGen {

	public CrushingRecipeGen(PackOutput output) {
		super(output);
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
							b -> b.require(blazeGoldRod()).output(0.5F, Items.BLAZE_POWDER).duration(250));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.CRUSHING;
	}
}
