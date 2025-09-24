package com.dudko.blazinghot.foundation.recipe;

import java.util.List;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidIngredient;
import com.google.common.base.Joiner;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

/**
 * Common methods for all Blazing Hot's recipe builders
 */
public interface IBlazingRecipeBuilder<P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, S extends IBlazingRecipeBuilder<P, R, S>> {


	R build();

	S require(Ingredient ingredient);

	S require(FluidIngredient fluidIngredient);

	S output(Fluid fluid, MultiAmount amount);

	S requireMultiple(Ingredient ingredient, int amount);

	S withConditions(List<LoadCondition<?>> conditions);

	List<LoadCondition<?>> getLoadConditions();

	ResourceLocation getRecipeId();

	default S require(TagKey<Fluid> fluidTag, MultiAmount amount) {
		return require(MultiFluidIngredient.fromTag(fluidTag, amount));
	}

	default S require(Fluid fluid, MultiAmount amount) {
		return require(MultiFluidIngredient.fromFluid(BuiltInRegistries.FLUID.wrapAsHolder(fluid), amount));
	}

	default S requireMultiple(TagKey<Item> tag, int amount) {
		return requireMultiple(Ingredient.of(tag), amount);
	}

	default S requireMultiple(ItemLike item, int amount) {
		return requireMultiple(Ingredient.of(item), amount);
	}

	default S withConditions(LoadCondition<?>... conditions) {
		return withConditions(List.of(conditions));
	}

	default void build(RecipeOutput consumer) {
		R recipe = build();
		IRecipeTypeInfo recipeType = recipe.getTypeInfo();
		ResourceLocation typeId = recipeType.getId();
		ResourceLocation id = getRecipeId().withPrefix(typeId.getPath() + "/");
		var errors = recipe.validate();
		if (!errors.isEmpty()) {
			errors.add(recipe.getClass().getSimpleName() + "with id " + id + " failed validation:");
			BlazingHot.LOGGER.warn(Joiner.on('\n').join(errors));
		}

		finishBuild(consumer, id, recipe, getLoadConditions());
	}

	@ExpectPlatform
	static <P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>> void finishBuild(RecipeOutput consumer, ResourceLocation id, R recipe, List<LoadCondition<?>> loadConditions) {
		throw new AssertionError();
	}

	@ExpectPlatform
	static <P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, S extends ProcessingRecipeBuilder<P, R, S>> S fluidOutput(S builder, Fluid fluid, MultiAmount amount) {
		throw new AssertionError();
	}
}
