package com.dudko.blazinghot.mixin.fabric;

import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ProcessingRecipeBuilder.ProcessingRecipeParams.class, remap = false)
public class ProcessingRecipeParamsMixin implements IProcessingRecipeParams {

    @Unique
    protected FluidIngredient blazinghot$fuelFluid;
    @Unique
    protected boolean blazinghot$formConversion;

    @Override
    public FluidIngredient blazinghot$getFuelFluid() {
        return blazinghot$fuelFluid;
    }

    @Override
    public boolean blazinghot$formConversion() {
        return blazinghot$formConversion;
    }

    @Override
    public void blazinghot$setFuelFluid(FluidIngredient fuelFluid) {
        blazinghot$fuelFluid = fuelFluid;
    }

    @Override
    public void blazinghot$setFormConversion(boolean formConversion) {
        blazinghot$formConversion = formConversion;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    protected void blazinghot$initCustomProperties(ResourceLocation id, CallbackInfo ci) {
        blazinghot$fuelFluid = FluidIngredient.EMPTY;
        blazinghot$formConversion = false;
    }

}
