package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.multiloader.MultiFluids;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.function.Consumer;

public class BlazingProcessingRecipeBuilder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {

    private boolean convertMeltable;
    private FluidIngredient fuel;

    public BlazingProcessingRecipeBuilder(ProcessingRecipeFactory<T> factory, ResourceLocation recipeId) {
        super(factory, recipeId);
    }

    /**
     * Converts droplets to milibuckets on Forge not by the 81:1 ratio but by {@link MultiFluids#MELTABLE_CONVERSION}
     */
    public BlazingProcessingRecipeBuilder<T> convertMeltable() {
        this.convertMeltable = true;
        return this;
    }

    public BlazingProcessingRecipeBuilder<T> requireFuel(FluidIngredient fuel) {
        this.fuel = fuel;
        return this;
    }

    public BlazingProcessingRecipeBuilder<T> requireFuel(FluidStack fluidStack) {
        return requireFuel(FluidIngredient.fromFluidStack(fluidStack));
    }

    public BlazingProcessingRecipeBuilder<T> requireFuel(Fluid fluid, long amount) {
        return requireFuel(FluidIngredient.fromFluid(fluid, amount));
    }

    public BlazingProcessingRecipeBuilder<T> requireFuel(TagKey<Fluid> tag, long amount) {
        return requireFuel(FluidIngredient.fromTag(tag, amount));
    }

    public BlazingProcessingRecipeBuilder<T> requireMultiple(Ingredient ingredient, int amount) {
        for (int i = 0; i < amount; i++) require(ingredient);
        return this;
    }

    public BlazingProcessingRecipeBuilder<T> requireMultiple(TagKey<Item> tag, int amount) {
        return requireMultiple(Ingredient.of(tag), amount);
    }

    public BlazingProcessingRecipeBuilder<T> requireMultiple(ItemLike item, int amount) {
        return requireMultiple(Ingredient.of(item), amount);
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new BlazingDataGenResult<>(build(), recipeConditions, convertMeltable, fuel));
    }

    public static class BlazingDataGenResult<S extends ProcessingRecipe<?>> extends DataGenResult<S> {

        private final boolean convertMeltable;
        private final FluidIngredient fuel;

        public BlazingDataGenResult(S recipe, List<ConditionJsonProvider> recipeConditions, boolean convertMeltable, FluidIngredient fuel) {
            super(recipe, recipeConditions);
            this.convertMeltable = convertMeltable;
            this.fuel = fuel;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (convertMeltable) json.addProperty("blazinghot:convertMeltable", true);
            if (fuel != null && fuel != FluidIngredient.EMPTY) json.add("blazinghot:fuel", fuel.serialize());
            super.serializeRecipeData(json);
        }
    }
}
