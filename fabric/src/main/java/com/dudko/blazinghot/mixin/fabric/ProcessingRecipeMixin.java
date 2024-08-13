package com.dudko.blazinghot.mixin.fabric;

import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipe;
import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ProcessingRecipe.class, remap = false)
public class ProcessingRecipeMixin implements IProcessingRecipe {

    @Unique
    FluidIngredient blazinghot$fuelFluid;
    @Unique
    boolean blazinghot$formConversion;

    @Override
    public FluidIngredient blazinghot$getFuelFluid() {
        return blazinghot$fuelFluid;
    }

    @Override
    public boolean blazinghot$formConversion() {
        return blazinghot$formConversion;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void blazinghot$setCustomProperties(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params, CallbackInfo ci) {
        this.blazinghot$fuelFluid = ((IProcessingRecipeParams) params).blazinghot$getFuelFluid();
        this.blazinghot$formConversion = ((IProcessingRecipeParams) params).blazinghot$formConversion();
    }

}
