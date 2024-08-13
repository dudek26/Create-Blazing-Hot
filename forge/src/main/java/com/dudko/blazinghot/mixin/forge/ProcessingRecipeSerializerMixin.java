package com.dudko.blazinghot.mixin.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.recipe.forge.IProcessingRecipeBuilder;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ProcessingRecipeSerializer.class)
public class ProcessingRecipeSerializerMixin<T extends ProcessingRecipe<?>> {


    /**
     * Converts fluid amounts in C:BH recipes' FluidIngredients from Fabric's droplets to milibuckets.
     */
    @ModifyArg(method = "readFromJson",
            remap = false,
            at = @At(value = "INVOKE",
                    remap = false,
                    target = "Lcom/simibubi/create/foundation/fluid/FluidIngredient;deserialize(Lcom/google/gson/JsonElement;)Lcom/simibubi/create/foundation/fluid/FluidIngredient;",
                    ordinal = 0))
    private JsonElement blazinghot$ingredientsPlatformedFluidAmount(JsonElement je, @Local JsonObject json,
                                                                    @Local(argsOnly = true) ResourceLocation recipeID) {
        JsonObject jsonObject = je.getAsJsonObject();
        if (json.has("blazinghot:convertMeltable") && GsonHelper.getAsBoolean(json, "blazinghot:convertMeltable")) {
            if (!jsonObject.has("amount")) throw new JsonSyntaxException("Fluid ingredient has to define an amount");
            long amount = GsonHelper.getAsLong(jsonObject, "amount");
            jsonObject.remove("amount");
            jsonObject.addProperty("amount", Mth.ceil(amount / MultiFluids.MELTABLE_CONVERSION));
        }

        else if (recipeID.getNamespace().equalsIgnoreCase(BlazingHot.ID)) {
            if (!jsonObject.has("amount"))
                throw new JsonSyntaxException("Fluid ingredient has to define an amount");

            long amount = GsonHelper.getAsLong(jsonObject, "amount");
            jsonObject.remove("amount");
            jsonObject.addProperty("amount", amount / 81);
        }
        return jsonObject;
    }

    /**
     * Converts fluid amounts in C:BH recipes' results from Fabric's droplets to milibuckets.
     */
    @ModifyArg(method = "readFromJson",
            remap = false,
            at = @At(value = "INVOKE",
                    remap = false,
                    target = "Lcom/simibubi/create/foundation/fluid/FluidHelper;deserializeFluidStack(Lcom/google/gson/JsonObject;)Lnet/minecraftforge/fluids/FluidStack;"))
    private JsonObject blazinghot$resultPlatformedFluidAmount(JsonObject jsonObject,
                                                              @Local(ordinal = 0) JsonObject json,
                                                              @Local(argsOnly = true) ResourceLocation recipeID) {
        if (json.has("blazinghot:convertMeltable") && GsonHelper.getAsBoolean(json, "blazinghot:convertMeltable")) {
            if (!jsonObject.has("amount")) throw new JsonSyntaxException("Fluid result has to define an amount");
            long amount = GsonHelper.getAsLong(jsonObject, "amount");
            jsonObject.remove("amount");
            jsonObject.addProperty("amount", amount / MultiFluids.MELTABLE_CONVERSION);
        }

        else if (recipeID.getNamespace().equalsIgnoreCase(BlazingHot.ID)) {
            if (!jsonObject.has("amount")) throw new JsonSyntaxException("Fluid result has to define an amount");

            long amount = GsonHelper.getAsLong(jsonObject, "amount");
            jsonObject.remove("amount");
            jsonObject.addProperty("amount", amount / 81);
        }
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "readFromJson",
            remap = false,
            at = @At(value = "INVOKE_ASSIGN",

                    target = "Lcom/simibubi/create/content/processing/recipe/ProcessingRecipeBuilder;withFluidOutputs(Lnet/minecraft/core/NonNullList;)Lcom/simibubi/create/content/processing/recipe/ProcessingRecipeBuilder;")
    )
    protected void blazinghot$readFuelFromJson(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<T> cir,
                                               @Local ProcessingRecipeBuilder<T> builder) {
        if (GsonHelper.isValidNode(json, "blazinghot:fuel")) {
            JsonObject blazinghot$fuelElement = json.get("blazinghot:fuel").getAsJsonObject();

            if (recipeId.getNamespace().equalsIgnoreCase(BlazingHot.ID)) {
                if (!blazinghot$fuelElement.has("amount"))
                    throw new JsonSyntaxException("Fuel has to define an amount");

                long blazinghot$amount = GsonHelper.getAsLong(blazinghot$fuelElement, "amount");
                blazinghot$fuelElement.remove("amount");
                blazinghot$fuelElement.addProperty("amount", blazinghot$amount / 81);
            }

            ((IProcessingRecipeBuilder<T>) builder).blazinghot$requireFuel(FluidIngredient.deserialize(
                    blazinghot$fuelElement));
        }
    }

}
