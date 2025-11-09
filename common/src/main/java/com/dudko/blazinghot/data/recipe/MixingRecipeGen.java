package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.andesite;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.cinderFlour;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.clayBall;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenAncientDebris;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenCopper;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenIron;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenZinc;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.netherCompound;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.netherEssence;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.netherFlora;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.netherrackDust;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.powderedObsidian;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.soulDust;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stoneDust;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.wheatFlour;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.BlazingStandardRecipeGen;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class MixingRecipeGen extends BlazingStandardRecipeGen<MixingRecipe> {

	public MixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);

		BlazingMetals.ALL.forEach(this::melting);
	}

	GeneratedRecipe
			NETHER_COMPOUND =
			create("nether_compound",
					b -> b
							.require(clayBall())
							.require(netherrackDust())
							.require(soulDust())
							.output(BlazingItems.NETHER_COMPOUND, 2)),
			NETHER_DOUGH =
					create("nether_dough",
							b -> b
									.require(netherCompound())
									.require(netherFlora())
									.require(wheatFlour())
									.output(BlazingItems.NETHER_DOUGH)),
			NETHERRACK_DUST =
					create("netherrack_dust",
							b -> b.require(cinderFlour()).require(stoneDust()).output(BlazingItems.NETHERRACK_DUST, 2)),
			MOLTEN_BLAZE_GOLD =
					bCreate("molten_blaze_gold",
							b -> b
									.output(BlazingMetals.BLAZE_GOLD.getFluid(), MultiAmount.ROD)
									.requireMultiple(netherEssence(), 2)
									.require(moltenGold(), MultiAmount.ROD)
									.mechanicalMixerOnly()
									.requiresHeat(HeatCondition.SUPERHEATED)
									.duration(200)),
			MOLTEN_NETHERITE =
					bCreate("molten_netherite",
							b -> b
									.output(BlazingMetals.NETHERITE.getFluid(), MultiAmount.INGOT.divide(4))
									.require(moltenGold(), MultiAmount.INGOT)
									.require(moltenAncientDebris(), MultiAmount.INGOT)
									.duration(200)
									.requiresHeat(HeatCondition.SUPERHEATED)),
			MOLTEN_ANDESITE =
					bCreate("molten_andesite",
							b -> b
									.output(BlazingMetals.ANDESITE.getFluid(), MultiAmount.ROD.multiply(3))
									.require(moltenIron(), MultiAmount.NUGGET)
									.require(andesite())
									.requiresHeat(HeatCondition.HEATED)),
			MOLTEN_BRASS =
					bCreate("molten_brass",
							b -> b
									.output(BlazingMetals.BRASS.getFluid(), MultiAmount.INGOT.multiply(2))
									.require(moltenCopper(), MultiAmount.INGOT)
									.require(moltenZinc(), MultiAmount.INGOT)
									.requiresHeat(HeatCondition.HEATED)),
			MOLTEN_STURDY_ALLOY =
					bCreate("molten_sturdy_alloy",
							b -> b
									.output(BlazingMetals.STURDY_ALLOY.getFluid(), MultiAmount.INGOT)
									.require(moltenIron(), MultiAmount.INGOT)
									.require(powderedObsidian())
									.require(netherCompound())
									.duration(200)
									.requiresHeat(HeatCondition.SUPERHEATED));

	private void melting(BlazingMetal metal) {
		for (BlazingForm form : metal.forms) {
			if (!form.flags.contains(BlazingForm.Flag.MELTING)) continue;
			if (!form.mechanicalMixerMeltable) continue;
			bCreate(form.getMeltingRecipeName(metal),
					b -> b
							.withConditions(form.getMeltingLoadConditions(metal))
							.output(metal.getFluid(), form.amount)
							.require(form.getMeltingIngredient(metal))
							.mechanicalMixerOnly()
							.duration(form.meltingTime * 3)
							.requiresHeat(HeatCondition.SUPERHEATED));
		}

	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MIXING;
	}
}
