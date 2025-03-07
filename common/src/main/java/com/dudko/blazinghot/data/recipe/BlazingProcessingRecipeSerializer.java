package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeBuilder.BlazingProcessingRecipeFactory;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidStack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;

import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class BlazingProcessingRecipeSerializer<T extends ProcessingRecipe<?>> extends ProcessingRecipeSerializer<T> {

	private final BlazingProcessingRecipeFactory<T> factory;

	public BlazingProcessingRecipeSerializer(BlazingProcessingRecipeFactory<T> factory) {
		//noinspection DataFlowIssue
		super(null);
		this.factory = factory;
	}

	@Override
	protected T readFromJson(ResourceLocation recipeId, JsonObject json) {
		BlazingProcessingRecipeBuilder<T> builder = new BlazingProcessingRecipeBuilder<>(factory, recipeId);
		NonNullList<Ingredient> ingredients = NonNullList.create();
		NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
		NonNullList<ProcessingOutput> results = NonNullList.create();
		NonNullList<MultiFluidStack> fluidResults = NonNullList.create();

		for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
			if (FluidIngredient.isFluidIngredient(je))
				fluidIngredients.add(FluidIngredient.deserialize(je));
			else
				ingredients.add(Ingredient.fromJson(je));
		}

		for (JsonElement je : GsonHelper.getAsJsonArray(json, "results")) {
			JsonObject jsonObject = je.getAsJsonObject();
			if (GsonHelper.isValidNode(jsonObject, "fluid"))
				fluidResults.add(FluidHelper.deserializeFluidStack(jsonObject));
			else
				results.add(ProcessingOutput.deserialize(je));
		}

		builder.withItemIngredients(ingredients)
			   .withItemOutputs(results)
			   .withFluidIngredients(fluidIngredients)
			   .withFluidOutputs(fluidResults);

		if (GsonHelper.isValidNode(json, "processingTime"))
			builder.duration(GsonHelper.getAsInt(json, "processingTime"));
		if (GsonHelper.isValidNode(json, "heatRequirement"))
			builder.requiresHeat(HeatCondition.deserialize(GsonHelper.getAsString(json, "heatRequirement")));

		T recipe = builder.build();
		recipe.readAdditional(json);
		return recipe;
	}

	@Override
	protected T readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		return super.readFromBuffer(recipeId, buffer);
	}
}
