package com.dudko.blazinghot.registry.forge;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class BlazingRecipeTypesImpl {

    public static IForgeRegistry<RecipeSerializer<?>> getSerializerRegistry() {
        return ForgeRegistries.RECIPE_SERIALIZERS;
    }

    public static IForgeRegistry<RecipeType<?>> getTypeRegistry() {
        return ForgeRegistries.RECIPE_TYPES;
    }

}
