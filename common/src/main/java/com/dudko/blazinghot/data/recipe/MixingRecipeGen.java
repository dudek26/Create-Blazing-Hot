package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.andesite;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.cinderFlour;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.clayBall;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenAncientDebris;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenCopper;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenIron;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenZinc;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherCompound;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherEssence;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherFlora;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherrackDust;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.powderedObsidian;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.soulDust;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.stoneDust;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.wheatFlour;

import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class MixingRecipeGen extends BlazingProcessingRecipeGen {

	public MixingRecipeGen(PackOutput output) {
		super(output);

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
							b -> b.require(cinderFlour()).require(stoneDust()).output(BlazingItems.NETHERRACK_DUST)),
			MOLTEN_BLAZE_GOLD =
					create("molten_blaze_gold",
							b -> b
									.requireMultiple(netherEssence(), 2)
									.require(moltenGold(), MultiAmount.ROD)
									.requiresHeat(HeatCondition.SUPERHEATED)
									.duration(200)
									.output(BlazingMetals.BLAZE_GOLD.getFluid().get(), MultiAmount.ROD)),
			MOLTEN_NETHERITE =
					create("molten_netherite",
							b -> b
									.require(moltenGold(), MultiAmount.INGOT)
									.require(moltenAncientDebris(), MultiAmount.INGOT)
									.duration(200)
									.requiresHeat(HeatCondition.SUPERHEATED)
									.output(BlazingMetals.NETHERITE.getFluid().get(), MultiAmount.INGOT.divide(4))),
			MOLTEN_ANDESITE =
					create("molten_andesite",
							b -> b
									.require(moltenIron(), MultiAmount.NUGGET)
									.require(andesite())
									.requiresHeat(HeatCondition.HEATED)
									.output(BlazingMetals.ANDESITE.getFluid().get(), MultiAmount.ROD.multiply(3))),
			MOLTEN_BRASS =
					create("molten_brass",
							b -> b
									.require(moltenCopper(), MultiAmount.INGOT)
									.require(moltenZinc(), MultiAmount.INGOT)
									.requiresHeat(HeatCondition.HEATED)
									.output(BlazingMetals.BRASS.getFluid().get(), MultiAmount.INGOT.multiply(2))),
			MOLTEN_STURDY_ALLOY =
					create("molten_sturdy_alloy",
							b -> b
									.require(moltenIron(), MultiAmount.INGOT)
									.require(powderedObsidian())
									.require(netherCompound())
									.duration(200)
									.requiresHeat(HeatCondition.SUPERHEATED)
									.output(BlazingMetals.STURDY_ALLOY.getFluid().get(), MultiAmount.INGOT));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MIXING;

	}

	private void melting(BlazingMetal metal) {
		for (BlazingForm form : metal.forms) {
			if (!form.flags.contains(BlazingForm.Flag.MELTING)) continue;
			if (!form.mechanicalMixerMeltable) continue;
			create(form.getMeltingRecipeName(metal),
					b -> b
							.withConditions(form.getMeltingLoadConditions(metal))
							.require(form.getMeltingIngredient(metal))
							.duration(form.meltingTime * 3)
							.requiresHeat(HeatCondition.SUPERHEATED)
							.output(metal.getFluid().get(), form.amount));
		}

	}

}
