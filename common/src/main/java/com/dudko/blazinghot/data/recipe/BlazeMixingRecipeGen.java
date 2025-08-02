package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.fuel;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.lava;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherEssence;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings({"unused"})
public class BlazeMixingRecipeGen extends BlazingProcessingRecipeGen {

	public BlazeMixingRecipeGen(PackOutput output) {
		super(output);

		for (BlazingMetal metal : BlazingMetals.ALL) {
			melting(metal);
		}
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return BlazingRecipeTypes.BLAZE_MIXING.get();
	}

	GeneratedRecipe
			NETHER_LAVA =
			create("nether_lava",
					b -> b
							.requireMultiple(netherEssence(), 2)
							.require(lava(), MultiAmount.fromBucketFraction(1, 10))
							.requiresHeat(HeatCondition.SUPERHEATED)
							.output(MultiRegistries.getFluidFromRegistry(BlazingHot.asResource("nether_lava")).get(),
									MultiAmount.fromBucketFraction(1, 10))),
			MOLTEN_BLAZE_GOLD =
					create("molten_blaze_gold",
							b -> b
									.requireMultiple(netherEssence(), 2)
									.requireFuel(fuel(), MultiAmount.fromBucketFraction(1, 20))
									.require(moltenGold(), MultiAmount.INGOT)
									.requiresHeat(HeatCondition.SUPERHEATED)
									.duration(200)
									.output(BlazingMetals.BLAZE_GOLD.getFluid().get(), MultiAmount.INGOT));

	private void melting(BlazingMetal metal) {
		for (BlazingForm form : metal.forms) {
			create(form.getMeltingRecipeName(metal),
					b -> b
							.withConditions(form.getLoadConditions(metal))
							.requireFuel(fuel(), form.fuelCost)
							.require(form.getMeltingIngredient(metal))
							.duration(form.meltingTime)
							.requiresHeat(HeatCondition.SUPERHEATED)
							.output(metal.getFluid().get(), form.amount));
		}

	}

}

