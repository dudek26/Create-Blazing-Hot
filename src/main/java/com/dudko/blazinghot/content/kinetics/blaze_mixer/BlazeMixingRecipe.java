package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.data.recipe.BlazeMixingRecipeBuilder;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class BlazeMixingRecipe extends BasinRecipe {

    @Nullable
    protected FluidIngredient fuelFluid;
    protected NonNullList<Ingredient> ingredients;
    protected NonNullList<ProcessingOutput> results;
    protected NonNullList<FluidIngredient> fluidIngredients;
    protected NonNullList<FluidStack> fluidResults;
    protected int processingDuration;
    protected HeatCondition requiredHeat;

    private Supplier<ItemStack> forcedResult;

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
        return getFuelFluid().test(fluidStack) && fluidStack.getAmount() >= getFuelFluid().getRequiredAmount();
    }

    public FluidIngredient getFuelFluid() {
        if (fuelFluid == null) return FluidIngredient.fromFluidStack(FluidStack.EMPTY);
        return fuelFluid;
    }

    @Override
    public NonNullList<FluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public NonNullList<FluidStack> getFluidResults() {
        return fluidResults;
    }

    @Override
    public int getProcessingDuration() {
        return processingDuration;
    }

    @Override
    public HeatCondition getRequiredHeat() {
        return requiredHeat;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public NonNullList<ProcessingOutput> getResults() {
        return results;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return getRollableResults().isEmpty() ? ItemStack.EMPTY : getRollableResults().get(0).getStack();
    }

    @Override
    public @NotNull List<ProcessingOutput> getRollableResults() {
        return results;
    }

    @Override
    public @NotNull List<ItemStack> rollResults() {
        return rollResults(this.getRollableResults());
    }

    @Override
    public void enforceNextResult(@NotNull Supplier<ItemStack> stack) {
        forcedResult = stack;
    }

    @Override
    public @NotNull List<ItemStack> rollResults(List<ProcessingOutput> rollableResults) {
        List<ItemStack> results = new ArrayList<>();
        for (int i = 0; i < rollableResults.size(); i++) {
            ProcessingOutput output = rollableResults.get(i);
            ItemStack stack = i == 0 && forcedResult != null ? forcedResult.get() : output.rollOutput();
            if (!stack.isEmpty()) results.add(stack);
        }
        return results;
    }

    public static long durationToFuelCost(int duration) {
        float recipeSpeed = 1;
        if (duration != 0) {
            recipeSpeed = duration / 100f;
        }
        return Mth.ceil(recipeSpeed * FluidConstants.fromBucketFraction(1, 40));
    }
}
