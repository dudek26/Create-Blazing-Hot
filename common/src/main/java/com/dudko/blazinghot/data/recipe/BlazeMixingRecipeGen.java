package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.fuel;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.lava;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherEssence;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipeBuilder;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeGen;
import com.dudko.blazinghot.registry.BlazingForms;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class BlazeMixingRecipeGen extends BlazingRecipeGen<ProcessingRecipeParams, BlazeMixingRecipe, BlazeMixingRecipeBuilder> {

	public BlazeMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);

		BlazingMetals.ALL.forEach(this::melting);
	}

	GeneratedRecipe
			NETHER_LAVA =
			create("nether_lava",
					b -> b
							.output(BuiltInRegistries.FLUID.get(BlazingHot.asResource("nether_lava")),
									MultiAmount.fromBucketFraction(1, 10))
							.requireMultiple(netherEssence(), 2)
							.require(lava(), MultiAmount.fromBucketFraction(1, 10))
							.requiresHeat(HeatCondition.SUPERHEATED)),
			MOLTEN_BLAZE_GOLD =
					create("molten_blaze_gold",
							b -> b
									.output(BlazingMetals.BLAZE_GOLD.getFluid(), MultiAmount.INGOT)
									.requireMultiple(netherEssence(), 2)
									.mixerFuel(fuel(), MultiAmount.fromBucketFraction(1, 20))
									.require(moltenGold(), MultiAmount.INGOT)
									.requiresHeat(HeatCondition.SUPERHEATED)
									.duration(200)), STURDY_MOLDS_MELTING = moldMelting();

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return BlazingRecipeTypes.BLAZE_MIXING;
	}

	@Override
	protected BlazeMixingRecipeBuilder getBuilder(ResourceLocation id) {
		return new BlazeMixingRecipeBuilder(BlazeMixingRecipe::new, id);
	}

	private void melting(BlazingMetal metal) {
		for (BlazingForm form : metal.forms) {
			if (!form.flags.contains(BlazingForm.Flag.MELTING)) continue;
			create(form.getMeltingRecipeName(metal),
					b -> b
							.output(metal.getFluid(), form.amount)
							.withConditions(form.getMeltingLoadConditions(metal))
							.mixerFuel(fuel(), form.fuelCost)
							.require(form.getMeltingIngredient(metal))
							.duration(form.meltingTime)
							.requiresHeat(HeatCondition.SUPERHEATED));
		}
	}

	private GeneratedRecipe moldMelting() {
		BlazingForm form = BlazingForms.STURDY_MOLD;
		return create("melting/sturdy_molds",
				b -> b
						.output(BlazingMetals.STURDY_ALLOY.getFluid(), form.amount)
						.mixerFuel(fuel(), form.fuelCost)
						.require(BlazingTags.Items.STURDY_MOLDS.tag())
						.duration(form.meltingTime)
						.requiresHeat(HeatCondition.SUPERHEATED));
	}
}
