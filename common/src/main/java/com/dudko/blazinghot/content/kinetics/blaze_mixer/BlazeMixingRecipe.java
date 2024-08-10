package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.util.FluidUtil;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dudko.blazinghot.util.FluidUtil.fromBucketFraction;

@SuppressWarnings("UnstableApiUsage")
public class BlazeMixingRecipe extends BasinRecipe {

    @Nullable
    protected FluidIngredient fuelFluid;

    public BlazeMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(BlazingRecipeTypes.BLAZE_MIXING.get(), params);
        fuelFluid = getFuelFluid(params);
    }

    @ExpectPlatform
    public static FluidIngredient getFuelFluid(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        throw new AssertionError();
    }

    @NotNull
    public FluidIngredient getFuelFluid() {
        if (fuelFluid == null) return FluidIngredient.EMPTY;
        return fuelFluid;
    }

    public NonNullList<ProcessingOutput> getResults() {
        return results;
    }

    /**
     * @apiNote Already platformed.
     */
    public static long durationToFuelCost(int duration) {
        float recipeSpeed = 1;
        if (duration != 0) {
            recipeSpeed = duration / 100f;
        }
        return Mth.ceil(recipeSpeed * BlazingConfigs.server().durationToFuelConversion.get());
    }

    /**
     * Used in melting recipes' datagen
     */
    @ApiStatus.Internal
    public static long defaultDurationToFuelCost(int duration) {
        float recipeSpeed = 1;
        if (duration != 0) {
            recipeSpeed = duration / 100f;
        }
        return Mth.ceil(recipeSpeed * fromBucketFraction(1, 40));
    }
}
