package com.dudko.blazinghot.data.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BlazingRecipeProvider extends RecipeProvider {

    protected final List<GeneratedRecipe> all = new ArrayList<>();

    public BlazingRecipeProvider(FabricDataOutput output) {
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
}
