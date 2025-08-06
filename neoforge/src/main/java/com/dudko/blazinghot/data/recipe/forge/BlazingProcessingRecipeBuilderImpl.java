package com.dudko.blazinghot.data.recipe.forge;

import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class BlazingProcessingRecipeBuilderImpl {

	public static <T extends ProcessingRecipe<?>> T platformBuild(BlazingProcessingRecipeBuilder<T> b) {
		ProcessingRecipeBuilder<T> builder = new ProcessingRecipeBuilder<>(b.factory, b.recipeId);
		builder.withItemIngredients(b.params.ingredients);
		builder.withFluidIngredients(b.params.fluidIngredients);
		builder.withItemOutputs(b.params.results);

		NonNullList<FluidStack>
				fluidStacks =
				b.params.fluidResults
						.stream()
						.map(stack -> new FluidStack(stack.getFluid(), (int) stack.getAmount().get(), stack.getTag()))
						.collect(NonNullList::create, NonNullList::add, NonNullList::addAll);

		builder.withFluidOutputs(fluidStacks);
		builder.requiresHeat(b.params.requiredHeat);
		builder.duration(b.params.processingDuration);
		if (b.params.keepHeldItem) builder.toolNotConsumed();

		return builder.build();
	}

}
