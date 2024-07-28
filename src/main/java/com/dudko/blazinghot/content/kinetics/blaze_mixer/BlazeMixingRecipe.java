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
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BlazeMixingRecipe extends BasinRecipe {

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
        return fuelFluid.test(fluidStack) && fluidStack.getAmount() >= fuelFluid.getRequiredAmount();
    }

    public FluidIngredient getFuelFluid() {
        if (fuelFluid == null) throw new IllegalStateException("Blaze Mixing Recipe: "
                + id.toString()
                + " has no fuel!");
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
        return getRollableResults().isEmpty() ? ItemStack.EMPTY
                                              : getRollableResults().get(0)
                                                                    .getStack();
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
            if (!stack.isEmpty())
                results.add(stack);
        }
        return results;
    }
}
