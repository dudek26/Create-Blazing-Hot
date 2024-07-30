package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.SimpleDatagenIngredient;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Pair;
import com.tterrag.registrate.util.DataIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Based on {@link ProcessingRecipeBuilder}
 *
 * @see ProcessingRecipeBuilder
 */
@SuppressWarnings({"UnusedReturnValue"})
public class BlazeMixingRecipeBuilder {

    public BlazeMixingRecipeParams params;
    public BlazeMixingRecipeFactory factory;
    public List<ConditionJsonProvider> recipeConditions;

    public BlazeMixingRecipeBuilder(BlazeMixingRecipeFactory factory, ResourceLocation recipeId) {
        params = new BlazeMixingRecipeParams(recipeId);
        recipeConditions = new ArrayList<>();
        this.factory = factory;
    }

    public BlazeMixingRecipeBuilder withItemIngredients(Ingredient... ingredients) {
        return withItemIngredients(NonNullList.of(Ingredient.EMPTY, ingredients));
    }

    public BlazeMixingRecipeBuilder withItemIngredients(NonNullList<Ingredient> ingredients) {
        params.ingredients = ingredients;
        return this;
    }

    public BlazeMixingRecipeBuilder withItemOutputs(NonNullList<ProcessingOutput> outputs) {
        params.results = outputs;
        return this;
    }

    public BlazeMixingRecipeBuilder withFluidIngredients(NonNullList<FluidIngredient> ingredients) {
        params.fluidIngredients = ingredients;
        return this;
    }

    public BlazeMixingRecipeBuilder withFluidOutputs(NonNullList<FluidStack> outputs) {
        params.fluidResults = outputs;
        return this;
    }

    public BlazeMixingRecipeBuilder duration(int ticks) {
        params.processingDuration = ticks;
        return this;
    }

    public BlazeMixingRecipeBuilder requiresHeat(HeatCondition condition) {
        params.requiredHeat = condition;
        return this;
    }

    public BlazeMixingRecipe build() {
        validateFluidAmounts();
        return factory.create(params);
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new BlazeMixingRecipeBuilder.DataGenResult(build(), recipeConditions));
    }

    public static final long[] SUS_AMOUNTS = {10, 250, 500, 1000};

    private void validateFluidAmounts() {
        for (FluidIngredient ingredient : params.fluidIngredients) {
            for (long amount : SUS_AMOUNTS) {
                if (ingredient.getRequiredAmount() == amount) {
                    Create.LOGGER.warn("Suspicious fluid amount in recipe [{}]: {}", params.id, amount);
                }
            }
        }
    }

    // Datagen shortcuts
    public BlazeMixingRecipeBuilder require(TagKey<Item> tag) {
        return require(Ingredient.of(tag));
    }

    public BlazeMixingRecipeBuilder require(ItemLike item) {
        return require(Ingredient.of(item));
    }

    public BlazeMixingRecipeBuilder require(Ingredient ingredient) {
        params.ingredients.add(ingredient);
        return this;
    }

    // fabric: custom ingredient support
    public BlazeMixingRecipeBuilder require(CustomIngredient ingredient) {
        return require(ingredient.toVanilla());
    }

    public BlazeMixingRecipeBuilder require(Mods mod, String id) {
        params.ingredients.add(new SimpleDatagenIngredient(mod, id));
        return this;
    }

    public BlazeMixingRecipeBuilder require(ResourceLocation ingredient) {
        params.ingredients.add(DataIngredient.ingredient(null, ingredient));
        return this;
    }

    public BlazeMixingRecipeBuilder require(Fluid fluid, long amount) {
        return require(FluidIngredient.fromFluid(fluid, amount));
    }

    public BlazeMixingRecipeBuilder require(TagKey<Fluid> fluidTag, long amount) {
        return require(FluidIngredient.fromTag(fluidTag, amount));
    }

    public BlazeMixingRecipeBuilder require(FluidIngredient ingredient) {
        params.fluidIngredients.add(ingredient);
        return this;
    }

    public BlazeMixingRecipeBuilder output(ItemLike item) {
        return output(item, 1);
    }

    public BlazeMixingRecipeBuilder output(float chance, ItemLike item) {
        return output(chance, item, 1);
    }

    public BlazeMixingRecipeBuilder output(ItemLike item, int amount) {
        return output(1, item, amount);
    }

    public BlazeMixingRecipeBuilder output(float chance, ItemLike item, int amount) {
        return output(chance, new ItemStack(item, amount));
    }

    public BlazeMixingRecipeBuilder output(ItemStack output) {
        return output(1, output);
    }

    public BlazeMixingRecipeBuilder output(float chance, ItemStack output) {
        return output(new ProcessingOutput(output, chance));
    }

    public BlazeMixingRecipeBuilder output(float chance, Mods mod, String id, int amount) {
        return output(new ProcessingOutput(Pair.of(mod.asResource(id), amount), chance));
    }

    public BlazeMixingRecipeBuilder output(float chance, ResourceLocation registryName, int amount) {
        return output(new ProcessingOutput(Pair.of(registryName, amount), chance));
    }

    public BlazeMixingRecipeBuilder output(ProcessingOutput output) {
        params.results.add(output);
        return this;
    }

    public BlazeMixingRecipeBuilder output(Fluid fluid, long amount) {
        fluid = FluidHelper.convertToStill(fluid);
        return output(new FluidStack(fluid, amount));
    }

    public BlazeMixingRecipeBuilder output(FluidStack fluidStack) {
        params.fluidResults.add(fluidStack);
        return this;
    }

    //
    public BlazeMixingRecipeBuilder whenModLoaded(String modid) {
        return withCondition(DefaultResourceConditions.allModsLoaded(modid));
    }

    public BlazeMixingRecipeBuilder whenModMissing(String modid) {
        return withCondition(DefaultResourceConditions.not(DefaultResourceConditions.allModsLoaded(modid)));
    }

    public BlazeMixingRecipeBuilder withCondition(ConditionJsonProvider condition) {
        recipeConditions.add(condition);
        return this;
    }

    public BlazeMixingRecipeBuilder requireFuel(TagKey<Fluid> fluidTag, long amount) {
        return requireFuel(FluidIngredient.fromTag(fluidTag, amount));
    }

    public BlazeMixingRecipeBuilder requireFuel(FluidIngredient ingredient) {
        params.fuelFluid = ingredient;
        return this;
    }

    public BlazeMixingRecipeBuilder requireFuel(FluidStack fluidStack) {
        return requireFuel(FluidIngredient.fromFluidStack(fluidStack));
    }

    @FunctionalInterface
    public interface BlazeMixingRecipeFactory {
        BlazeMixingRecipe create(BlazeMixingRecipeParams params);
    }

    public static class BlazeMixingRecipeParams extends ProcessingRecipeBuilder.ProcessingRecipeParams {

        public ResourceLocation id;
        public NonNullList<Ingredient> ingredients;
        public NonNullList<ProcessingOutput> results;
        public NonNullList<FluidIngredient> fluidIngredients;
        public NonNullList<FluidStack> fluidResults;
        public int processingDuration;
        public HeatCondition requiredHeat;
        public FluidIngredient fuelFluid;

        protected BlazeMixingRecipeParams(ResourceLocation id) {
            super(id);
            this.id = id;
            ingredients = NonNullList.create();
            results = NonNullList.create();
            fluidIngredients = NonNullList.create();
            fluidResults = NonNullList.create();
            fuelFluid = FluidIngredient.EMPTY;
            processingDuration = 0;
            requiredHeat = HeatCondition.NONE;
        }
    }

    public static class DataGenResult implements FinishedRecipe {

        private final List<ConditionJsonProvider> recipeConditions;
        private final BlazeMixingRecipeSerializer serializer;
        private final ResourceLocation id;
        private final BlazeMixingRecipe recipe;

        @SuppressWarnings("unchecked")
        public DataGenResult(BlazeMixingRecipe recipe, List<ConditionJsonProvider> recipeConditions) {
            this.recipe = recipe;
            this.recipeConditions = recipeConditions;
            IRecipeTypeInfo recipeType = this.recipe.getTypeInfo();
            ResourceLocation typeId = recipeType.getId();

            if (!(recipeType.getSerializer() instanceof BlazeMixingRecipeSerializer)) throw new IllegalStateException(
                    "Cannot datagen BlazeMixingRecipeSerializer of type: " + typeId);

            this.id = new ResourceLocation(recipe.getId().getNamespace(),
                    typeId.getPath() + "/" + recipe.getId().getPath());
            this.serializer = (BlazeMixingRecipeSerializer) recipe.getSerializer();
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            serializer.write(json, recipe);
            if (recipeConditions.isEmpty()) return;

            JsonArray conds = new JsonArray();
            recipeConditions.forEach(c -> conds.add(c.toJson())); // FabricDataGenHelper.addConditions(json, recipeConditions.toArray());?
            json.add(ResourceConditions.CONDITIONS_KEY, conds);
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
