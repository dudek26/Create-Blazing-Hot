package com.dudko.blazinghot.registry.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class BlazingRecipeTypesImpl {
    public static Registry<RecipeSerializer<?>> getSerializerRegistry() {
        return BuiltInRegistries.RECIPE_SERIALIZER;
    }

    public static Registry<RecipeType<?>> getTypeRegistry() {
        return BuiltInRegistries.RECIPE_TYPE;
    }


}
