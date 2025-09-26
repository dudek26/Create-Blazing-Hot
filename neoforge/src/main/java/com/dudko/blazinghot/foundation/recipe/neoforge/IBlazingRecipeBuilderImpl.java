package com.dudko.blazinghot.foundation.recipe.neoforge;

import java.util.List;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.neoforge.LoadConditionImpl;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.IBlazingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * @see IBlazingRecipeBuilder
 */
public class IBlazingRecipeBuilderImpl {

	public static <P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>> void finishBuild(RecipeOutput consumer, ResourceLocation id, R recipe, List<LoadCondition<?>> loadConditions) {
		consumer.accept(id,
				recipe,
				null,
				loadConditions
						.stream()
						.map(c -> (LoadConditionImpl<?>) c)
						.map(LoadConditionImpl::getNeoForgeCondition)
						.toArray(ICondition[]::new));
	}

	public static <P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, S extends ProcessingRecipeBuilder<P, R, S>> S fluidOutput(S builder, Fluid fluid, MultiAmount amount) {
		return builder.output(new FluidStack(fluid, amount.millibuckets()));
	}

}
