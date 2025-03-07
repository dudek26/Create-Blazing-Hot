package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidIngredient;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.DataGenResult;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.foundation.data.SimpleDatagenIngredient;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.util.DataIngredient;

import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.data.Pair;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.function.Consumer;

/**
 * From {@link ProcessingRecipeBuilder}
 */
public class BlazingProcessingRecipeBuilder<T extends ProcessingRecipe<?>> {

	public ResourceLocation recipeId;
	public ProcessingRecipeFactory<T> factory;
	public BlazingProcessingRecipeParams params;

	public BlazingProcessingRecipeBuilder(ProcessingRecipeFactory<T> factory, ResourceLocation recipeId) {
		this.factory = factory;
		this.recipeId = recipeId;
		params = new BlazingProcessingRecipeParams(recipeId);
	}

	public BlazingProcessingRecipeBuilder<T> withItemIngredients(Ingredient... ingredients) {
		return withItemIngredients(NonNullList.of(Ingredient.EMPTY, ingredients));
	}

	public BlazingProcessingRecipeBuilder<T> withItemIngredients(NonNullList<Ingredient> ingredients) {
		params.ingredients = ingredients;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> withSingleItemOutput(ItemStack output) {
		return withItemOutputs(new ProcessingOutput(output, 1));
	}

	public BlazingProcessingRecipeBuilder<T> withItemOutputs(ProcessingOutput... outputs) {
		return withItemOutputs(NonNullList.of(ProcessingOutput.EMPTY, outputs));
	}

	public BlazingProcessingRecipeBuilder<T> withItemOutputs(NonNullList<ProcessingOutput> outputs) {
		params.results = outputs;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> withFluidIngredients(FluidIngredient... ingredients) {
		return withFluidIngredients(NonNullList.of(FluidIngredient.EMPTY, ingredients));
	}

	public BlazingProcessingRecipeBuilder<T> withFluidIngredients(NonNullList<FluidIngredient> ingredients) {
		params.fluidIngredients = ingredients;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> withFluidOutputs(MultiFluidStack... outputs) {
		return withFluidOutputs(NonNullList.of(MultiFluidStack.EMPTY, outputs));
	}

	public BlazingProcessingRecipeBuilder<T> withFluidOutputs(NonNullList<MultiFluidStack> outputs) {
		params.fluidResults = outputs;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> duration(int ticks) {
		params.processingDuration = ticks;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> averageProcessingDuration() {
		return duration(100);
	}

	public BlazingProcessingRecipeBuilder<T> requiresHeat(HeatCondition condition) {
		params.requiredHeat = condition;
		return this;
	}

	public T build() {
		return platformBuild(this);
	}

	public void build(Consumer<FinishedRecipe> consumer) {
		consumer.accept(new BlazingDataGenResult<>(build(), params.fuel));
	}

	@ExpectPlatform
	public static <T extends ProcessingRecipe<?>> T platformBuild(BlazingProcessingRecipeBuilder<T> builder) {
		throw new AssertionError();
	}

	// Datagen shortcuts

	public BlazingProcessingRecipeBuilder<T> require(TagKey<Item> tag) {
		return require(Ingredient.of(tag));
	}

	public BlazingProcessingRecipeBuilder<T> require(ItemLike item) {
		return require(Ingredient.of(item));
	}

	public BlazingProcessingRecipeBuilder<T> require(Ingredient ingredient) {
		params.ingredients.add(ingredient);
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> require(Mods mod, String id) {
		params.ingredients.add(new SimpleDatagenIngredient(mod, id));
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> require(ResourceLocation ingredient) {
		params.ingredients.add(DataIngredient.ingredient(null, ingredient));
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> require(Fluid fluid, long amount) {
		return require(fluid, MultiAmount.standard(amount));
	}

	public BlazingProcessingRecipeBuilder<T> require(Fluid fluid, MultiAmount amount) {
		return require(MultiFluidIngredient.fromFluid(fluid, amount));
	}

	public BlazingProcessingRecipeBuilder<T> require(TagKey<Fluid> fluidTag, long amount) {
		return require(fluidTag, MultiAmount.standard(amount));
	}

	public BlazingProcessingRecipeBuilder<T> require(TagKey<Fluid> fluidTag, MultiAmount amount) {
		return require(MultiFluidIngredient.fromTag(fluidTag, amount));
	}

	public BlazingProcessingRecipeBuilder<T> require(FluidIngredient ingredient) {
		params.fluidIngredients.add(ingredient);
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> requireMultiple(Ingredient ingredient, int amount) {
		for (int i = 0; i < amount; i++) {
			require(ingredient);
		}
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> requireMultiple(TagKey<Item> tag, int amount) {
		return requireMultiple(Ingredient.of(tag), amount);
	}

	public BlazingProcessingRecipeBuilder<T> requireMultiple(ItemLike item, int amount) {
		return requireMultiple(Ingredient.of(item), amount);
	}

	public BlazingProcessingRecipeBuilder<T> requireFuel(FluidIngredient fuel) {
		params.fuel = fuel;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> requireFuel(FluidStack stack) {
		return requireFuel(MultiFluidIngredient.fromStack(stack));
	}

	public BlazingProcessingRecipeBuilder<T> requireFuel(Fluid fluid, long amount) {
		return requireFuel(MultiFluidIngredient.fromFluid(fluid, MultiAmount.standard(amount)));
	}

	public BlazingProcessingRecipeBuilder<T> requireFuel(TagKey<Fluid> tag, long amount) {
		return requireFuel(MultiFluidIngredient.fromTag(tag, MultiAmount.standard(amount)));
	}

	public BlazingProcessingRecipeBuilder<T> output(ItemLike item) {
		return output(item, 1);
	}

	public BlazingProcessingRecipeBuilder<T> output(float chance, ItemLike item) {
		return output(chance, item, 1);
	}

	public BlazingProcessingRecipeBuilder<T> output(ItemLike item, int amount) {
		return output(1, item, amount);
	}

	public BlazingProcessingRecipeBuilder<T> output(float chance, ItemLike item, int amount) {
		return output(chance, new ItemStack(item, amount));
	}

	public BlazingProcessingRecipeBuilder<T> output(ItemStack output) {
		return output(1, output);
	}

	public BlazingProcessingRecipeBuilder<T> output(float chance, ItemStack output) {
		return output(new ProcessingOutput(output, chance));
	}

	public BlazingProcessingRecipeBuilder<T> output(float chance, Mods mod, String id, int amount) {
		return output(new ProcessingOutput(Pair.of(mod.asResource(id), amount), chance));
	}

	public BlazingProcessingRecipeBuilder<T> output(ResourceLocation id) {
		return output(1, id, 1);
	}

	public BlazingProcessingRecipeBuilder<T> output(Mods mod, String id) {
		return output(1, mod.asResource(id), 1);
	}

	public BlazingProcessingRecipeBuilder<T> output(float chance, ResourceLocation registryName, int amount) {
		return output(new ProcessingOutput(Pair.of(registryName, amount), chance));
	}

	public BlazingProcessingRecipeBuilder<T> output(ProcessingOutput output) {
		params.results.add(output);
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> output(Fluid fluid, MultiAmount amount) {
		fluid = FluidHelper.convertToStill(fluid);
		return output(new MultiFluidStack(fluid, amount));
	}

	public BlazingProcessingRecipeBuilder<T> output(Fluid fluid, long amount) {
		return output(fluid, MultiAmount.standard(amount));
	}

	public BlazingProcessingRecipeBuilder<T> output(MultiFluidStack fluidStack) {
		params.fluidResults.add(fluidStack);
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> toolNotConsumed() {
		params.keepHeldItem = true;
		return this;
	}

	public static class BlazingProcessingRecipeParams {

		public ResourceLocation id;
		public NonNullList<Ingredient> ingredients;
		public NonNullList<ProcessingOutput> results;
		public NonNullList<FluidIngredient> fluidIngredients;
		public NonNullList<MultiFluidStack> fluidResults;
		public int processingDuration;
		public HeatCondition requiredHeat;
		public FluidIngredient fuel;

		public boolean keepHeldItem;

		protected BlazingProcessingRecipeParams(ResourceLocation id) {
			this.id = id;
			ingredients = NonNullList.create();
			results = NonNullList.create();
			fluidIngredients = NonNullList.create();
			fluidResults = NonNullList.create();
			processingDuration = 0;
			requiredHeat = HeatCondition.NONE;
			keepHeldItem = false;
		}

	}

	public static class BlazingDataGenResult<S extends ProcessingRecipe<?>> extends DataGenResult<S> {

		private final FluidIngredient fuel;

		// TODO: add conditions
		public BlazingDataGenResult(S recipe, FluidIngredient fuel) {
			super(recipe, List.of());
			this.fuel = fuel;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (fuel != null && fuel != FluidIngredient.EMPTY) json.add("blazinghot:fuel", fuel.serialize());

			super.serializeRecipeData(json);
		}
	}

}
