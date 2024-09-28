package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.content.metal.MoltenMetals.ALL;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.ironIngot;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.netherCompound;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.powderedObsidian;

import java.util.List;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class CompactingRecipeGen extends BlazingProcessingRecipeGen {

	public CompactingRecipeGen(PackOutput output) {
		super(output);
	}

	List<GeneratedRecipe>
			ALL_MOLTEN_COMPACTING_RECIPES =
			ALL
					.stream()
					.map(metal -> create(metal.moltenName(),
							b -> b
									.convertMeltable()
									.require(metal.fluidTag(), metal.compactingResult().getSecond())
									.output(metal.compactingResult().getFirst())))
					.toList();

	GeneratedRecipe
			STURDY_ALLOY =
			create("sturdy_alloy",
					b -> b
							.requireMultiple(netherCompound(), 2)
							.require(powderedObsidian())
							.require(ironIngot())
							.output(BlazingItems.STURDY_ALLOY)
							.requiresHeat(HeatCondition.SUPERHEATED));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.COMPACTING;
	}

}
