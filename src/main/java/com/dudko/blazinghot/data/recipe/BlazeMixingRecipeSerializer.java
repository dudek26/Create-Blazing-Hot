package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class BlazeMixingRecipeSerializer implements RecipeSerializer<BlazeMixingRecipe> {

    private final BlazeMixingRecipeBuilder.BlazeMixingRecipeFactory factory;

    public BlazeMixingRecipeSerializer(BlazeMixingRecipeBuilder.BlazeMixingRecipeFactory factory) {
        this.factory = factory;
    }

    protected void writeToJson(JsonObject json, BlazeMixingRecipe recipe) {
        JsonArray jsonIngredients = new JsonArray();
        JsonArray jsonOutputs = new JsonArray();

        recipe.getIngredients().forEach(i -> jsonIngredients.add(i.toJson()));
        recipe.getFluidIngredients().forEach(i -> jsonIngredients.add(i.serialize()));

        recipe.getResults().forEach(o -> jsonOutputs.add(o.serialize()));
        recipe.getFluidResults().forEach(o -> jsonOutputs.add(FluidHelper.serializeFluidStack(o)));

        json.add("ingredients", jsonIngredients);
        json.add("results", jsonOutputs);

        int processingDuration = recipe.getProcessingDuration();
        if (processingDuration > 0) json.addProperty("processingTime", processingDuration);

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE) json.addProperty("heatRequirement", requiredHeat.serialize());

        FluidIngredient fuel = recipe.getFuelFluid();
        json.add("fuel", fuel.serialize());

        recipe.writeAdditional(json);
    }

    protected BlazeMixingRecipe readFromJson(ResourceLocation recipeId, JsonObject json) {
        BlazeMixingRecipeBuilder builder = new BlazeMixingRecipeBuilder(factory, recipeId);
        NonNullList<Ingredient> ingredients = NonNullList.create();
        NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
        NonNullList<ProcessingOutput> results = NonNullList.create();
        NonNullList<FluidStack> fluidResults = NonNullList.create();
        FluidStack fuel = new FluidStack(Fluids.LAVA, 8100);

        for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
            if (FluidIngredient.isFluidIngredient(je)) fluidIngredients.add(FluidIngredient.deserialize(je));
            else ingredients.add(Ingredient.fromJson(je));
        }

        for (JsonElement je : GsonHelper.getAsJsonArray(json, "results")) {
            JsonObject jsonObject = je.getAsJsonObject();
            if (GsonHelper.isValidNode(jsonObject, "fluid")) fluidResults.add(FluidHelper.deserializeFluidStack(
                    jsonObject));
            else results.add(ProcessingOutput.deserialize(je));
        }

        JsonObject fuelObject = GsonHelper.getNonNull(json, "fuel").getAsJsonObject();
        if (GsonHelper.isValidNode(fuelObject, "fluid")) fuel = FluidHelper.deserializeFluidStack(fuelObject);

        builder
                .withItemIngredients(ingredients)
                .withItemOutputs(results)
                .withFluidIngredients(fluidIngredients)
                .withFluidOutputs(fluidResults)
                .requireFuel(fuel);

        if (GsonHelper.isValidNode(json, "processingTime")) builder.duration(GsonHelper.getAsInt(json,
                "processingTime"));
        if (GsonHelper.isValidNode(json, "heatRequirement"))
            builder.requiresHeat(HeatCondition.deserialize(GsonHelper.getAsString(json, "heatRequirement")));

        BlazeMixingRecipe recipe = builder.build();
        recipe.readAdditional(json);
        return recipe;
    }

    protected void writeToBuffer(FriendlyByteBuf buffer, BlazeMixingRecipe recipe) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        NonNullList<FluidIngredient> fluidIngredients = recipe.getFluidIngredients();
        NonNullList<ProcessingOutput> outputs = recipe.getResults();
        NonNullList<FluidStack> fluidOutputs = recipe.getFluidResults();
        FluidIngredient fuel = recipe.getFuelFluid();

        buffer.writeVarInt(ingredients.size());
        ingredients.forEach(i -> i.toNetwork(buffer));
        buffer.writeVarInt(fluidIngredients.size());
        fluidIngredients.forEach(i -> i.write(buffer));

        buffer.writeVarInt(outputs.size());
        outputs.forEach(o -> o.write(buffer));
        buffer.writeVarInt(fluidOutputs.size());
        fluidOutputs.forEach(o -> o.writeToPacket(buffer));

        fuel.write(buffer);
        buffer.writeVarInt(recipe.getProcessingDuration());
        buffer.writeVarInt(recipe.getRequiredHeat().ordinal());

        recipe.writeAdditional(buffer);
    }

    protected BlazeMixingRecipe readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        NonNullList<FluidIngredient> fluidIngredients = NonNullList.create();
        NonNullList<ProcessingOutput> results = NonNullList.create();
        NonNullList<FluidStack> fluidResults = NonNullList.create();

        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            ingredients.add(Ingredient.fromNetwork(buffer));

        size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            fluidIngredients.add(FluidIngredient.read(buffer));

        size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            results.add(ProcessingOutput.read(buffer));

        size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            fluidResults.add(FluidStack.readFromPacket(buffer));

        BlazeMixingRecipe recipe = new BlazeMixingRecipeBuilder(factory, recipeId)
                .withItemIngredients(ingredients)
                .withItemOutputs(results)
                .withFluidIngredients(fluidIngredients)
                .withFluidOutputs(fluidResults)
                .requireFuel(FluidIngredient.read(buffer))
                .duration(buffer.readVarInt())
                .requiresHeat(HeatCondition.values()[buffer.readVarInt()])
                .build();
        recipe.readAdditional(buffer);
        return recipe;
    }

    public final void write(JsonObject json, BlazeMixingRecipe recipe) {
        writeToJson(json, recipe);
    }

    @Override
    public final @NotNull BlazeMixingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        return readFromJson(id, json);
    }

    @Override
    public final void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull BlazeMixingRecipe recipe) {
        writeToBuffer(buffer, recipe);
    }

    @Override
    public final @NotNull BlazeMixingRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
        return readFromBuffer(id, buffer);
    }

    public BlazeMixingRecipeBuilder.BlazeMixingRecipeFactory getFactory() {
        return factory;
    }
}
