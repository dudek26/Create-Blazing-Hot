package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.diamond;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.lava;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.netherEssence;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stoneDust;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.water;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipeBuilder;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeGen;
import com.dudko.blazinghot.registry.BlazingFluids;
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
				.requireMultiple(netherEssence(), 2)
				.require(lava(), MultiAmount.fromBucketFraction(1, 10))
				.requiresHeat(HeatCondition.SUPERHEATED)
				.output(BuiltInRegistries.FLUID.get(BlazingHot.asResource("nether_lava")),
					MultiAmount.fromBucketFraction(1, 10))),
		MOLTEN_BLAZE_GOLD =
			create("molten_blaze_gold",
				b -> b
					.requireMultiple(netherEssence(), 2)
					.mixerFuel(MultiAmount.fromBucketFraction(1, 20).get())
					.require(moltenGold(), MultiAmount.INGOT)
					.requiresHeat(HeatCondition.SUPERHEATED)
					.duration(200)
					.output(BlazingMetals.BLAZE_GOLD.getFluid(), MultiAmount.INGOT)),
		STURDY_MOLDS_MELTING =
			moldMelting(),
		CRYSTAL_MIXTURE =
			create("crystal_mixture",
				b -> b
					.requireMultiple(diamond(), 2)
					.requireMultiple(stoneDust(), 3)
					.require(water(), MultiAmount.fromBucketFraction(1, 8))
					.requiresHeat(HeatCondition.HEATED)
					.output(BlazingFluids.getCrystalMixture().getSource(),
						MultiAmount.fromBucketFraction(1, 8)));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return BlazingRecipeTypes.BLAZE_MIXING;
	}

	@Override
	protected BlazeMixingRecipeBuilder getBuilder(ResourceLocation id) {
		return new BlazeMixingRecipeBuilder(BlazeMixingRecipe::create, id);
	}

	private void melting(BlazingMetal metal) {
		for (BlazingForm form : metal.forms) {
			if (!form.flags.contains(BlazingForm.Flag.MELTING)) continue;
			create(form.getMeltingRecipeName(metal),
				b -> b
					.withConditions(form.getMeltingLoadConditions(metal))
					.mixerFuel(form.fuelCost.get())
					.require(form.getMeltingIngredient(metal))
					.duration(form.meltingTime)
					.requiresHeat(HeatCondition.SUPERHEATED)
					.output(metal.getFluid(), form.amount));
		}
	}

	private GeneratedRecipe moldMelting() {
		BlazingForm form = BlazingForms.STURDY_MOLD;
		return create("melting/sturdy_molds",
			b -> b
				.mixerFuel(form.fuelCost.get())
				.require(BlazingTags.Items.STURDY_MOLDS.tag())
				.duration(form.meltingTime)
				.requiresHeat(HeatCondition.SUPERHEATED)
				.output(BlazingMetals.STURDY_ALLOY.getFluid(), form.amount));
	}
}
