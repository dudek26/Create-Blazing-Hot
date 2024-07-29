package com.dudko.blazinghot.data.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class BlazingPRB<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {

    public BlazingPRB(ProcessingRecipeFactory<T> factory, ResourceLocation recipeId) {
        super(factory, recipeId);
    }

    public BlazingPRB<T> requireMultiple(TagKey<Item> tag, int count) {
        return requireMultiple(Ingredient.of(tag), count);
    }

    public BlazingPRB<T> requireMultiple(ItemLike item, int count) {
        return requireMultiple(Ingredient.of(item), count);
    }

    public BlazingPRB<T> requireMultiple(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            require(ingredient);
        }
        return this;
    }
}
