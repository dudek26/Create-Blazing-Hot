package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.fuel;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.lava;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.moltenGold;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.netherEssence;
import static com.dudko.blazinghot.util.ListUtil.compactLists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.Forms;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings({"unused"})
public class BlazeMixingRecipeGen extends BlazingProcessingRecipeGen {

	public BlazeMixingRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return BlazingRecipeTypes.BLAZE_MIXING.get();
	}

	List<GeneratedRecipe> ALL_MELTING_RECIPES = compactLists(MoltenMetals.ALL.stream().map(this::melting).toList());

	GeneratedRecipe
			NETHER_LAVA =
			create("nether_lava",
					b -> b
							.requireMultiple(netherEssence(), 2)
							.require(lava(), MultiAmount.fromBucketFraction(1, 10))
							.requiresHeat(HeatCondition.SUPERHEATED)
							.output(MultiRegistries.getFluidFromRegistry(BlazingHot.asResource("nether_lava")).get(),
									MultiAmount.BUCKET)),
			MOLTEN_BLAZE_GOLD =
					create("molten_blaze_gold",
							b -> b
									.requireMultiple(netherEssence(), 2)
									.requireFuel(fuel(), MultiAmount.fromBucketFraction(1, 20))
									.require(moltenGold(), MultiAmount.INGOT)
									.requiresHeat(HeatCondition.SUPERHEATED)
									.duration(200)
									.output(MoltenMetals.BLAZE_GOLD.fluid().get(), MultiAmount.INGOT));

	private GeneratedRecipe melting(String name, Ingredient ingredient, Fluid result, MultiAmount amount, int duration, long fuelCost, Collection<LoadCondition<?>> conditions) {
		return create("melting/" + name,
				(b) -> b
						.requireFuel(fuel(), fuelCost)
						.withConditions(conditions)
						.require(ingredient)
						.duration(duration)
						.requiresHeat(HeatCondition.SUPERHEATED)
						.output(result, amount));
	}

	private GeneratedRecipe melting(ResourceLocation itemLocation, Fluid result, MultiAmount amount, int duration, long fuelCost, Collection<LoadCondition<?>> conditions) {
		return melting(itemLocation.getPath(),
				Ingredient.of(MultiRegistries.getItemFromRegistry(itemLocation).get()),
				result,
				amount,
				duration,
				fuelCost,
				conditions);
	}

	private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, MultiAmount amount, int duration, long fuelCost, Collection<LoadCondition<?>> conditions) {
		return melting(tag.location().getPath(), Ingredient.of(tag), result, amount, duration, fuelCost, conditions);
	}

	private List<GeneratedRecipe> melting(MoltenMetal metal) {
		List<GeneratedRecipe> recipes = new ArrayList<>();
		metal
				.supportedForms()
				.forEach(form -> recipes.add(melting(form.tag(metal),
						metal.fluid().get(),
						form.amount,
						form.processingTime,
						form.fuelCost,
						metal.getLoadConditions())));
		metal
				.customForms()
				.forEach(form -> recipes.add(melting(form.customLocation,
						metal.fluid().get(),
						form.amount,
						form.processingTime,
						form.fuelCost,
						metal.getLoadConditions())));
		for (Forms optional : metal.optionalForms) {
			List<LoadCondition<?>> conditions = new ArrayList<>(metal.getLoadConditions());
			conditions.add(DefaultLoadConditions.tagsPopulated(optional.tag(metal)));
			recipes.add(melting(optional.tag(metal),
					metal.fluid().get(),
					optional.amount,
					optional.processingTime,
					optional.fuelCost,
					conditions));
		}
		metal.compatForms.forEach((form, mod) -> recipes.add(melting(form.tag(metal),
				metal.fluid().get(),
				form.amount,
				form.processingTime,
				form.fuelCost,
				metal.getLoadConditions(form, mod))));
		return recipes;
	}

}

