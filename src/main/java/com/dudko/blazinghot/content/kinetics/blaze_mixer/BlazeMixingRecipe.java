package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.data.recipe.BlazeMixingRecipeBuilder;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlazeMixingRecipe extends BasinRecipe {

    public FluidIngredient fuelFluid;
    public NonNullList<Ingredient> ingredients;
    public NonNullList<ProcessingOutput> results;
    public NonNullList<FluidIngredient> fluidIngredients;
    public NonNullList<FluidStack> fluidResults;
    public int processingDuration;
    public HeatCondition requiredHeat;

    public BlazeMixingRecipe(BlazeMixingRecipeBuilder.BlazeMixingRecipeParams params) {
        super(BlazingRecipeTypes.BLAZE_MIXING, params);
        fuelFluid = params.fuelFluid;
        results = params.results;
        ingredients = params.ingredients;
        fluidIngredients = params.fluidIngredients;
        fluidResults = params.fluidResults;
        processingDuration = params.processingDuration;
        requiredHeat = params.requiredHeat;
    }

    public boolean hasFuel(FluidStack fluidStack) {
        return fuelFluid.test(fluidStack) && fluidStack.getAmount() >= fuelFluid.getRequiredAmount();
    }

    public FluidIngredient getFuelFluid() {
        if (fuelFluid == null) throw new IllegalStateException("Blaze Mixing Recipe: "
                + id.toString()
                + " has no fuel!");
        return fuelFluid;
    }

}
