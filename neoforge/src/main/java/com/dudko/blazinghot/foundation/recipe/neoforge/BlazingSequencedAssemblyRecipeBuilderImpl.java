package com.dudko.blazinghot.foundation.recipe.neoforge;

import java.util.List;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.neoforge.LoadConditionImpl;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * @see com.dudko.blazinghot.foundation.recipe.BlazingSequencedAssemblyRecipeBuilder
 */
public class BlazingSequencedAssemblyRecipeBuilderImpl {

	public static void finishBuild(RecipeOutput consumer, ResourceLocation id, SequencedAssemblyRecipe recipe, List<LoadCondition<?>> loadConditions) {
		consumer.accept(id,
				recipe,
				null,
				loadConditions
						.stream()
						.map(c -> (LoadConditionImpl<?>) c)
						.map(LoadConditionImpl::getNeoForgeCondition)
						.toArray(ICondition[]::new));
	}

}
