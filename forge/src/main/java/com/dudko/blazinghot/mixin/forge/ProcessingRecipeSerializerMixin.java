package com.dudko.blazinghot.mixin.forge;

import com.dudko.blazinghot.BlazingHot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(ProcessingRecipeSerializer.class)
public class ProcessingRecipeSerializerMixin {


    /**
     * Converts fluid amounts in C:BH recipes' FluidIngredients from Fabric's droplets to milibuckets.
     */
    @ModifyArg(method = "readFromJson",
            remap = false,
            at = @At(value = "INVOKE",
                    remap = false,
                    target = "Lcom/simibubi/create/foundation/fluid/FluidIngredient;deserialize(Lcom/google/gson/JsonElement;)Lcom/simibubi/create/foundation/fluid/FluidIngredient;",
                    ordinal = 0))
    private JsonElement blazinghot$ingredientsPlatformedFluidAmount(JsonElement je, @Local(argsOnly = true) ResourceLocation recipeID) {
        if (recipeID.getNamespace().equalsIgnoreCase(BlazingHot.ID)) {
            JsonObject fluidIngredientObject = je.getAsJsonObject();

            if (!fluidIngredientObject.has("amount"))
                throw new JsonSyntaxException("Fluid ingredient has to define an amount");

            int amount = GsonHelper.getAsInt(fluidIngredientObject, "amount");
            fluidIngredientObject.remove("amount");
            fluidIngredientObject.addProperty("amount", amount / 81);
            return fluidIngredientObject;
        }
        else return je;
    }

    /**
     * Converts fluid amounts in C:BH recipes' results from Fabric's droplets to milibuckets.
     */
    @ModifyArg(method = "readFromJson",
            remap = false,
            at = @At(value = "INVOKE",
                    remap = false,
                    target = "Lcom/simibubi/create/foundation/fluid/FluidHelper;deserializeFluidStack(Lcom/google/gson/JsonObject;)Lnet/minecraftforge/fluids/FluidStack;"))
    private JsonObject blazinghot$resultPlatformedFluidAmount(JsonObject json, @Local(argsOnly = true) ResourceLocation recipeID) {
        if (recipeID.getNamespace().equalsIgnoreCase(BlazingHot.ID)) {
            if (!json.has("amount"))
                throw new JsonSyntaxException("Fluid ingredient has to define an amount");

            int amount = GsonHelper.getAsInt(json, "amount");
            json.remove("amount");
            json.addProperty("amount", amount / 81);
        }
        return json;
    }

}
