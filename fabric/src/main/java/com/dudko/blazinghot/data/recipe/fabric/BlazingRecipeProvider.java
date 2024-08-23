package com.dudko.blazinghot.data.recipe.fabric;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public abstract class BlazingRecipeProvider extends RecipeProvider {

	protected final List<GeneratedRecipe> all = new ArrayList<>();

	public BlazingRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	public void buildRecipes(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer) {
		all.forEach(c -> c.register(finishedRecipeConsumer));
	}

	protected GeneratedRecipe register(GeneratedRecipe recipe) {
		all.add(recipe);
		return recipe;
	}

	@FunctionalInterface
	public interface GeneratedRecipe {
		void register(Consumer<FinishedRecipe> consumer);
	}

	protected static String foodMetalName(String name) {
		return switch (name.toLowerCase()) {
			case "gold" -> "golden";
			case "blaze_gold" -> "blaze";
			default -> name;
		};
	}
}
