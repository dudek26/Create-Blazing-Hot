package com.dudko.blazinghot.data.recipe;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings({"unused"})
public class CastingRecipeGen extends BlazingProcessingRecipeGen {

	public CastingRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return BlazingRecipeTypes.CASTING.get();
	}

	List<GeneratedRecipe> ALL_CASTING_RECIPES = new ArrayList<>();

	GeneratedRecipe
			BLAZE_GOLD_INGOT =
			create("blaze_gold_ingot",
					b -> b
							.require(Molds.INGOT.get(Molds.MoldType.STURDY))
							.require(MoltenMetals.BLAZE_GOLD.fluidTag(), MultiAmount.INGOT)
							.castingDuration(MultiAmount.INGOT)
							.toolNotConsumed(Molds.MoldType.STURDY.reusable)
							.output(BlazingItems.BLAZE_GOLD_INGOT));

}

