package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.mixin.IProcessingRecipeParams;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.util.FluidUtil;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class BlazeMixingRecipe extends BasinRecipe {

    @Nullable
    protected FluidIngredient fuelFluid;

    public BlazeMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(BlazingRecipeTypes.BLAZE_MIXING.get(), params);
        fuelFluid = ((IProcessingRecipeParams) params).blazinghot$getFuelFluid();
    }

    public FluidIngredient getFuelFluid() {
        if (fuelFluid == null) return FluidIngredient.EMPTY;
        return fuelFluid;
    }

    public NonNullList<ProcessingOutput> getResults() {
        return results;
    }

    public static long durationToFuelCost(int duration) {
        float recipeSpeed = 1;
        if (duration != 0) {
            recipeSpeed = duration / 100f;
        }
        return Mth.ceil(recipeSpeed * FluidUtil.fromBucketFraction(1, 40));
    }
}
