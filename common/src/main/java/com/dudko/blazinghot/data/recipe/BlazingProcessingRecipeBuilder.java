package com.dudko.blazinghot.data.recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.LoadConditionHelper;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidIngredient;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.SimpleDatagenIngredient;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.tterrag.registrate.util.DataIngredient;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

/**
 * From {@link ProcessingRecipeBuilder}
 */
public class BlazingProcessingRecipeBuilder<T extends ProcessingRecipe<?>> {

	public final ResourceLocation recipeId;
	public final ProcessingRecipeFactory<T> factory;
	public final BlazingProcessingRecipeParams params;

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

	public BlazingProcessingRecipeBuilder<T> coolingDuration(int ticks) {
		params.coolingDuration = ticks;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> averageProcessingDuration() {
		return duration(100);
	}

	public BlazingProcessingRecipeBuilder<T> castingDuration(long fluidAmount) {
		// TODO: config for these
		float coolingFactor = 1.5f;
		int baseDuration = 200;
		int duration = (int) (fluidAmount / MultiAmount.INGOT.get()) * baseDuration;
		return duration(duration).coolingDuration((int) (duration * coolingFactor));
	}

	public BlazingProcessingRecipeBuilder<T> castingDuration(MultiAmount amount) {
		return castingDuration(amount.get());
	}

	public BlazingProcessingRecipeBuilder<T> requiresHeat(HeatCondition condition) {
		params.requiredHeat = condition;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> withCondition(LoadCondition<?> condition) {
		params.conditions.add(condition);
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> withConditions(LoadCondition<?>... conditions) {
		return withConditions(List.of(conditions));
	}

	public BlazingProcessingRecipeBuilder<T> withConditions(Collection<LoadCondition<?>> conditions) {
		params.conditions.addAll(conditions);
		return this;
	}

	public T build() {
		return platformBuild(this);
	}

	public void build(Consumer<FinishedRecipe> consumer) {
		consumer.accept(new BlazingDataGenResult<>(build(), params.fuel, params.coolingDuration, params.conditions));
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

	public BlazingProcessingRecipeBuilder<T> requireFuel(Fluid fluid, long amount) {
		return requireFuel(fluid, MultiAmount.standard(amount));
	}

	public BlazingProcessingRecipeBuilder<T> requireFuel(Fluid fluid, MultiAmount amount) {
		return requireFuel(MultiFluidIngredient.fromFluid(fluid, amount));
	}

	public BlazingProcessingRecipeBuilder<T> requireFuel(TagKey<Fluid> tag, long amount) {
		return requireFuel(tag, MultiAmount.standard(amount));
	}

	public BlazingProcessingRecipeBuilder<T> requireFuel(TagKey<Fluid> tag, MultiAmount amount) {
		return requireFuel(MultiFluidIngredient.fromTag(tag, amount));
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

	public BlazingProcessingRecipeBuilder<T> toolNotConsumed(boolean bool) {
		params.keepHeldItem = bool;
		return this;
	}

	public BlazingProcessingRecipeBuilder<T> toolNotConsumed() {
		return toolNotConsumed(true);
	}

	public static class BlazingProcessingRecipeParams {

		public ResourceLocation id;
		public NonNullList<Ingredient> ingredients;
		public NonNullList<ProcessingOutput> results;
		public NonNullList<FluidIngredient> fluidIngredients;
		public NonNullList<MultiFluidStack> fluidResults;
		public int processingDuration;
		public int coolingDuration;
		public HeatCondition requiredHeat;

		public FluidIngredient fuel;
		public NonNullList<LoadCondition<?>> conditions;

		public boolean keepHeldItem;

		protected BlazingProcessingRecipeParams(ResourceLocation id) {
			this.id = id;
			ingredients = NonNullList.create();
			results = NonNullList.create();
			fluidIngredients = NonNullList.create();
			fluidResults = NonNullList.create();
			processingDuration = 0;
			coolingDuration = 0;
			requiredHeat = HeatCondition.NONE;
			keepHeldItem = false;
			fuel = FluidIngredient.EMPTY;
			conditions = NonNullList.create();
		}

	}

	@ParametersAreNonnullByDefault
	public static class BlazingDataGenResult<S extends ProcessingRecipe<?>> implements FinishedRecipe {

		private final List<LoadCondition<?>> recipeConditions = new ArrayList<>();
		private final ProcessingRecipeSerializer<S> serializer;
		private final ResourceLocation id;
		private final S recipe;
		private final FluidIngredient fuel;
		private final int coolingDuration;

		@SuppressWarnings("unchecked")
		public BlazingDataGenResult(S recipe, FluidIngredient fuel, int coolingDuration, List<LoadCondition<?>> conditions) {
			this.recipe = recipe;
			this.recipeConditions.addAll(conditions);
			IRecipeTypeInfo recipeType = this.recipe.getTypeInfo();
			ResourceLocation typeId = recipeType.getId();

			if (!(recipeType.getSerializer() instanceof ProcessingRecipeSerializer))
				throw new IllegalStateException("Cannot datagen ProcessingRecipe of type: " + typeId);

			this.id =
					new ResourceLocation(recipe.getId().getNamespace(),
							typeId.getPath() + "/" + recipe.getId().getPath());
			this.serializer = (ProcessingRecipeSerializer<S>) recipe.getSerializer();

			this.fuel = fuel;
			this.coolingDuration = coolingDuration;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (fuel != null && fuel != FluidIngredient.EMPTY) json.add("mixerFuel", fuel.serialize());
			if (coolingDuration > 0) json.addProperty("coolingDuration", coolingDuration);

			serializer.write(json, recipe);

			if (recipeConditions.isEmpty()) return;
			JsonArray conds = new JsonArray();
			recipeConditions.forEach(c -> conds.add(c.toJson()));
			json.add(LoadConditionHelper.conditionsKey(), conds);
		}

		@Override
		public @NotNull ResourceLocation getId() {
			return id;
		}

		@Override
		public @NotNull RecipeSerializer<?> getType() {
			return serializer;
		}

		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}

}
