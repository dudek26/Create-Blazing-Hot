package com.dudko.blazinghot.data.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.casting.Molds.Mold;
import com.dudko.blazinghot.content.casting.Molds.MoldType;
import com.dudko.blazinghot.content.metal.Forms;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.data.conditions.LoadCondition;
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

	List<GeneratedRecipe> ALL_CASTING_RECIPES = castingRecipes();

	GeneratedRecipe
			BLAZE_GOLD_INGOT =
			create("blaze_gold_ingot",
					b -> b
							.require(Molds.INGOT.get(MoldType.STURDY))
							.require(MoltenMetals.BLAZE_GOLD.fluidTag(), MultiAmount.INGOT)
							.castingDuration(MultiAmount.INGOT)
							.toolNotConsumed(MoldType.STURDY.reusable)
							.output(BlazingItems.BLAZE_GOLD_INGOT));

	private GeneratedRecipe casting(MoltenMetal metal, Forms form, MoldType moldType, Collection<LoadCondition<?>> conditions) {
		Mold mold = form.mold;
		if (mold == null) return null;
		return create(moldType + "/" + mold + "/" + metal.name,
				(b) -> b
						.withConditions(conditions)
						.require(mold.get(moldType))
						.require(metal.fluidTag(), metal.getAmount(form))
						.castingDuration(form.amount)
						.toolNotConsumed(moldType.reusable)
						.output(metal.getLocation(form)));
	}

	private List<GeneratedRecipe> casting(MoltenMetal metal, Forms form, Collection<LoadCondition<?>> conditions, Mods outputMod) {
		return Arrays.stream(MoldType.values()).map(moldType -> casting(metal, form, moldType, conditions)).toList();
	}

	private List<GeneratedRecipe> castingRecipes() {
		return MoltenMetals.ALL.stream().map(this::castingRecipes).flatMap(Collection::stream).toList();
	}

	private List<GeneratedRecipe> castingRecipes(MoltenMetal metal) {
		List<GeneratedRecipe> recipes = new ArrayList<>();

		metal.supportedForms.forEach((form, mod) -> recipes.addAll(casting(metal,
				form,
				metal.getLoadConditions(),
				mod)));
		metal.customForms.forEach((form, mod) -> recipes.addAll(casting(metal, form, metal.getLoadConditions(), mod)));
		metal.compatForms.forEach((form, mod) -> recipes.addAll(casting(metal,
				form,
				metal.getLoadConditions(form, mod),
				mod)));

		for (Map.Entry<Forms, Mods> optional : metal.optionalForms.entrySet()) {
			List<LoadCondition<?>> conditions = new ArrayList<>(metal.getLoadConditions());
			conditions.add(DefaultLoadConditions.tagsPopulated(optional.getKey().tag(metal)));
			recipes.addAll(casting(metal, optional.getKey(), conditions, optional.getValue()));
		}


		return recipes;
	}

}

