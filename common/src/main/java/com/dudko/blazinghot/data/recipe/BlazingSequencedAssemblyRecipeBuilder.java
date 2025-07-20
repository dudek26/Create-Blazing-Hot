package com.dudko.blazinghot.data.recipe;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.LoadConditionHelper;
import com.dudko.blazinghot.mixin.accessor.SequencedAssemblyRecipeBuilderAccessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeSerializer;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class BlazingSequencedAssemblyRecipeBuilder extends SequencedAssemblyRecipeBuilder {

	protected NonNullList<LoadCondition<?>> conditions = NonNullList.create();

	public BlazingSequencedAssemblyRecipeBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder require(TagKey<Item> tag) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.require(tag);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder require(ItemLike ingredient) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.require(ingredient);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder require(Ingredient ingredient) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.require(ingredient);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder transitionTo(ItemLike item) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.transitionTo(item);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder addOutput(ItemLike item, float weight) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.addOutput(item, weight);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder addOutput(ItemStack item, float weight) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.addOutput(item, weight);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder loops(int loops) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.loops(loops);
	}

	public <T extends ProcessingRecipe<?>> BlazingSequencedAssemblyRecipeBuilder addBlazingStep(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, UnaryOperator<BlazingProcessingRecipeBuilder<T>> builder) {
		BlazingProcessingRecipeBuilder<T>
				recipeBuilder =
				new BlazingProcessingRecipeBuilder<>(factory, new ResourceLocation("dummy"));
		Item placeHolder = self().getRecipe().getTransitionalItem().getItem();
		self()
				.getRecipe()
				.getSequence()
				.add(new SequencedRecipe<>((builder.apply(recipeBuilder
						.require(placeHolder)
						.output(placeHolder))).build()));
		return this;
	}

	@Override
	public <T extends ProcessingRecipe<?>> BlazingSequencedAssemblyRecipeBuilder addStep(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, UnaryOperator<ProcessingRecipeBuilder<T>> builder) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.addStep(factory, builder);
	}

	public BlazingSequencedAssemblyRecipeBuilder withCondition(LoadCondition<?> condition) {
		conditions.add(condition);
		return this;
	}

	public BlazingSequencedAssemblyRecipeBuilder withConditions(List<LoadCondition<?>> conditions) {
		this.conditions.addAll(conditions);
		return this;
	}

	public BlazingSequencedAssemblyRecipeBuilder withConditions(LoadCondition<?>... conditions) {
		this.conditions.addAll(Arrays.asList(conditions));
		return this;
	}

	private SequencedAssemblyRecipeBuilderAccessor self() {
		return (SequencedAssemblyRecipeBuilderAccessor) this;
	}

	@Override
	public void build(Consumer<FinishedRecipe> consumer) {
		consumer.accept(new BlazingDataGenResult(build(), conditions));
	}

	@ParametersAreNonnullByDefault
	public static class BlazingDataGenResult implements FinishedRecipe {

		private final SequencedAssemblyRecipe recipe;
		private final List<LoadCondition<?>> recipeConditions;
		private final ResourceLocation id;
		private final SequencedAssemblyRecipeSerializer serializer;

		public BlazingDataGenResult(SequencedAssemblyRecipe recipe, List<LoadCondition<?>> recipeConditions) {
			this.recipeConditions = recipeConditions;
			this.recipe = recipe;
			String namespace = recipe.getId().getNamespace();
			String path = AllRecipeTypes.SEQUENCED_ASSEMBLY.getId().getPath();
			this.id = new ResourceLocation(namespace, path + "/" + recipe.getId().getPath());
			this.serializer = (SequencedAssemblyRecipeSerializer) recipe.getSerializer();
		}

		public void serializeRecipeData(JsonObject json) {
			this.serializer.write(json, this.recipe);

			if (this.recipeConditions.isEmpty()) return;
			JsonArray conds = new JsonArray();
			this.recipeConditions.forEach((c) -> conds.add(c.toJson()));
			json.add(LoadConditionHelper.conditionsKey(), conds);
		}

		public @NotNull ResourceLocation getId() {
			return this.id;
		}

		public @NotNull RecipeSerializer<?> getType() {
			return this.serializer;
		}

		public JsonObject serializeAdvancement() {
			return null;
		}

		public ResourceLocation getAdvancementId() {
			return null;
		}
	}

}
