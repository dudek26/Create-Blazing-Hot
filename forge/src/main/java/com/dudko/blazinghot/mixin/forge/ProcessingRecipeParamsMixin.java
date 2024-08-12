package com.dudko.blazinghot.mixin.forge;

import com.dudko.blazinghot.data.recipe.forge.IProcessingRecipeParams;
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

    @Override
    public FluidIngredient blazinghot$getFuelFluid() {
        return blazinghot$fuelFluid;
    }

    @Override
    public void blazinghot$setFuelFluid(FluidIngredient fuelFluid) {
        blazinghot$fuelFluid = fuelFluid;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    protected void blazinghot$setFuelFluidInit(ResourceLocation id, CallbackInfo ci) {
        blazinghot$fuelFluid = FluidIngredient.EMPTY;
    }

}
