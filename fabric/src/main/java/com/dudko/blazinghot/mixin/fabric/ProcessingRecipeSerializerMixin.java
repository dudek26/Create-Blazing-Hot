package com.dudko.blazinghot.mixin.fabric;

import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipe;
import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipeBuilder;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProcessingRecipeSerializer.class)
public class ProcessingRecipeSerializerMixin<T extends ProcessingRecipe<?>> {

    @Inject(method = "writeToJson(Lcom/google/gson/JsonObject;Lcom/simibubi/create/content/processing/recipe/ProcessingRecipe;)V",
            at = @At("HEAD"), remap = false)
    protected void blazinghot$writeCustomToJson(JsonObject json, T recipe, CallbackInfo ci) {
        IProcessingRecipe iRecipe = (IProcessingRecipe) recipe;

        boolean blazinghot$convertFluidAmounts = iRecipe.blazinghot$formConversion();
        if (blazinghot$convertFluidAmounts)
            json.addProperty("blazinghot:convertMeltable", true);

        FluidIngredient blazinghot$fuel = iRecipe.blazinghot$getFuelFluid();
        if (blazinghot$fuel != FluidIngredient.EMPTY) {
            json.add("blazinghot:fuel", blazinghot$fuel.serialize());
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "readFromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lcom/simibubi/create/content/processing/recipe/ProcessingRecipe;",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lcom/simibubi/create/content/processing/recipe/ProcessingRecipeBuilder;withFluidOutputs(Lnet/minecraft/core/NonNullList;)Lcom/simibubi/create/content/processing/recipe/ProcessingRecipeBuilder;"))
    protected void blazinghot$readCustomFromJson(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<T> cir,
                                                 @Local ProcessingRecipeBuilder<T> builder) {
        if (GsonHelper.isValidNode(json, "blazinghot:fuel"))
            ((IProcessingRecipeBuilder<T>) builder).blazinghot$requireFuel(FluidIngredient.deserialize(json.get(
                    "blazinghot:fuel")));
    }

}
