package com.dudko.blazinghot.mixin.fabric;

import com.dudko.blazinghot.data.recipe.IProcessingRecipe;
import com.dudko.blazinghot.data.recipe.IProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProcessingRecipe.class)
public class ProcessingRecipeMixin implements IProcessingRecipe {

    @Unique
    FluidIngredient blazinghot$fuelFluid;

    @Override
    public FluidIngredient blazinghot$getFuelFluid() {
        return blazinghot$fuelFluid;
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    protected void blazinghot$setFuelFluid(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params, CallbackInfo ci) {
        this.blazinghot$fuelFluid = ((IProcessingRecipeParams) params).blazinghot$getFuelFluid();
    }

}
