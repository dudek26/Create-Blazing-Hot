package com.dudko.blazinghot.foundation.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.foundation.mixin.accessor.SequencedAssemblyRecipeBuilderAccessor;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;

public class BlazingSequencedAssemblyRecipeBuilder extends SequencedAssemblyRecipeBuilder {

	protected List<LoadCondition<?>> recipeConditions;

	public BlazingSequencedAssemblyRecipeBuilder(ResourceLocation id) {
		super(id);
		recipeConditions = new ArrayList<>();
	}

	protected SequencedAssemblyRecipe getRecipe() {
		return ((SequencedAssemblyRecipeBuilderAccessor) this).blazinghot$getRecipe();
	}

	public <B extends BlazingStandardRecipeBuilder<?>> BlazingSequencedAssemblyRecipeBuilder addBlazingStep(Function<ResourceLocation, B> factory, Function<B, ? extends StandardProcessingRecipe.Builder<?>> builder) {
		B recipeBuilder = factory.apply(ResourceLocation.withDefaultNamespace("dummy"));
		Item placeHolder = getRecipe().getTransitionalItem().getItem();
		getRecipe()
				.getSequence()
				.add(new SequencedRecipe<>(builder
						.apply((B) recipeBuilder.require(placeHolder).output(placeHolder))
						.build()));
		return this;
	}

	public <R extends StandardProcessingRecipe<?>> BlazingSequencedAssemblyRecipeBuilder addStep(StandardProcessingRecipe.Factory<R> factory, UnaryOperator<StandardProcessingRecipe.Builder<R>> builder) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.addStep(factory, builder);
	}

	@Override
	public <R extends ItemApplicationRecipe> BlazingSequencedAssemblyRecipeBuilder addStep(ItemApplicationRecipe.Factory<R> factory, UnaryOperator<ItemApplicationRecipe.Builder<R>> builder) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.addStep(factory, builder);
	}

	@Override
	public <B extends ProcessingRecipeBuilder<?, ?, B>> BlazingSequencedAssemblyRecipeBuilder addStep(Function<ResourceLocation, B> factory, UnaryOperator<B> builder) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.addStep(factory, builder);
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder require(ItemLike ingredient) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.require(Ingredient.of(ingredient));
	}

	@Override
	public BlazingSequencedAssemblyRecipeBuilder require(TagKey<Item> tag) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.require(Ingredient.of(tag));
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
	public BlazingSequencedAssemblyRecipeBuilder loops(int loops) {
		return (BlazingSequencedAssemblyRecipeBuilder) super.loops(loops);
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
	public void build(RecipeOutput consumer) {
		RecipeHolder<SequencedAssemblyRecipe> holder = build();

		ResourceLocation
				id =
				ResourceLocation.fromNamespaceAndPath(holder.id().getNamespace(),
						AllRecipeTypes.SEQUENCED_ASSEMBLY.getId().getPath() + "/" + holder.id().getPath());

		finishBuild(consumer, id, holder.value(), recipeConditions);
	}

	@ExpectPlatform
	public static void finishBuild(RecipeOutput consumer, ResourceLocation id, SequencedAssemblyRecipe recipe, List<LoadCondition<?>> loadConditions) {
		throw new AssertionError();
	}

}

