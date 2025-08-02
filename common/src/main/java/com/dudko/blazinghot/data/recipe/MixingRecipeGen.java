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
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.soulDust;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.stoneDust;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.wheatFlour;
import static com.dudko.blazinghot.util.ListUtil.compactLists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.content.metal.Forms;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings("unused")
public class MixingRecipeGen extends BlazingProcessingRecipeGen {

	public MixingRecipeGen(PackOutput output) {
		super(output);
	}

	List<GeneratedRecipe>
			ALL_MELTING_RECIPES =
			compactLists(MoltenMetals.ALL.stream().filter(m -> m.mechanicalMixerMeltable).map(this::melting).toList());

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
									.output(MoltenMetals.BLAZE_GOLD.fluid().get(), MultiAmount.ROD)),
			MOLTEN_NETHERITE =
					create("molten_netherite",
							b -> b
									.require(moltenGold(), MultiAmount.INGOT)
									.require(moltenAncientDebris(), MultiAmount.INGOT)
									.duration(200)
									.requiresHeat(HeatCondition.SUPERHEATED)
									.output(MoltenMetals.NETHERITE.fluid().get(), MultiAmount.INGOT.divide(4))),
			MOLTEN_ANDESITE =
					create("molten_andesite",
							b -> b
									.require(moltenIron(), MultiAmount.NUGGET)
									.require(andesite())
									.requiresHeat(HeatCondition.HEATED)
									.output(MoltenMetals.ANDESITE.fluid().get(), MultiAmount.ROD.multiply(3))),
			MOLTEN_BRASS =
					create("molten_brass",
							b -> b
									.require(moltenCopper(), MultiAmount.INGOT)
									.require(moltenZinc(), MultiAmount.INGOT)
									.requiresHeat(HeatCondition.HEATED)
									.output(MoltenMetals.BRASS.fluid().get(), MultiAmount.INGOT.multiply(2)));

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MIXING;

	}

	private void melting(BlazingMetal metal, BlazingForm form) {
	}

	private GeneratedRecipe melting(String name, Ingredient ingredient, Fluid result, MultiAmount amount, int duration, Collection<LoadCondition<?>> conditions) {
		return create("melting/" + name,
				(b) -> b
						.withConditions(conditions)
						.require(ingredient)
						.duration(duration)
						.requiresHeat(HeatCondition.SUPERHEATED)
						.output(result, amount));
	}

	private GeneratedRecipe melting(ResourceLocation itemLocation, Fluid result, MultiAmount amount, int duration, Collection<LoadCondition<?>> conditions) {
		return melting(itemLocation.getPath(),
				Ingredient.of(MultiRegistries.getItemFromRegistry(itemLocation).get()),
				result,
				amount,
				duration,
				conditions);
	}

	private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, MultiAmount amount, int duration, Collection<LoadCondition<?>> conditions) {
		return melting(tag.location().getPath(), Ingredient.of(tag), result, amount, duration, conditions);
	}

	private List<GeneratedRecipe> melting(MoltenMetal metal) {
		List<GeneratedRecipe> recipes = new ArrayList<>();
		metal
				.supportedForms()
				.stream()
				.filter(f -> f.mechanicalMixerMeltable)
				.forEach(form -> recipes.add(melting(form.tag(metal),
						metal.fluid().get(),
						form.amount,
						form.processingTime * 3,
						metal.getLoadConditions())));
		metal
				.customForms()
				.stream()
				.filter(f -> f.mechanicalMixerMeltable)
				.forEach(form -> recipes.add(melting(form.customLocation,
						metal.fluid().get(),
						form.amount,
						form.processingTime * 3,
						metal.getLoadConditions())));
		for (Forms optional : metal.optionalForms.keySet()) {
			if (!optional.mechanicalMixerMeltable) continue;
			List<LoadCondition<?>> conditions = new ArrayList<>(metal.getLoadConditions());
			conditions.add(DefaultLoadConditions.tagsPopulated(optional.tag(metal)));
			recipes.add(melting(optional.tag(metal),
					metal.fluid().get(),
					optional.amount,
					optional.processingTime * 3,
					conditions));
		}
		metal.compatForms.forEach((form, mod) -> {
			if (!form.mechanicalMixerMeltable) return;
			recipes.add(melting(form.tag(metal),
					metal.fluid().get(),
					form.amount,
					form.processingTime * 3,
					metal.getLoadConditions(form, mod)));
		});
		return recipes;
	}

}
