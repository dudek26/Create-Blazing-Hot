package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.data.recipe.IProcessingRecipe;
import com.dudko.blazinghot.data.recipe.IProcessingRecipeBuilder;
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
public abstract class ProcessingRecipeSerializerMixin<T extends ProcessingRecipe<?>> {

    @Inject(method = "writeToJson", at = @At("HEAD"), remap = false)
    protected void blazinghot$writeFuelToJson(JsonObject json, T recipe, CallbackInfo ci) {
        FluidIngredient blazinghot$fuel = ((IProcessingRecipe) recipe).blazinghot$getFuelFluid();
        if (blazinghot$fuel != FluidIngredient.EMPTY) {
            json.add("fuel", blazinghot$fuel.serialize());
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "readFromJson",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lcom/simibubi/create/content/processing/recipe/ProcessingRecipeBuilder;withFluidOutputs(Lnet/minecraft/core/NonNullList;)Lcom/simibubi/create/content/processing/recipe/ProcessingRecipeBuilder;"),
            remap = false)
    protected void blazinghot$readFuelFromJson(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<T> cir, @Local ProcessingRecipeBuilder<T> builder) {
        if (GsonHelper.isValidNode(json, "fuel"))
            ((IProcessingRecipeBuilder<T>) builder).blazinghot$requireFuel(FluidIngredient.deserialize(json.get("fuel")));
    }



}
