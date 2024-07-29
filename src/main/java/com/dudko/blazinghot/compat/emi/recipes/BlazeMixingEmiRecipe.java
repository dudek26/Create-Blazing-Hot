package com.dudko.blazinghot.compat.emi.recipes;

import com.dudko.blazinghot.compat.emi.BlazingEmiAnimations;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.compat.emi.CreateEmiPlugin;
import com.simibubi.create.compat.emi.recipes.basin.BasinEmiRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BlazeMixingEmiRecipe extends BasinEmiRecipe {

    private final List<EmiIngredient> fuels;

    public BlazeMixingEmiRecipe(EmiRecipeCategory category, BasinRecipe recipe) {
        super(category, recipe, category != CreateEmiPlugin.AUTOMATIC_SHAPELESS);
        FluidIngredient fuelFluid;
        if (recipe instanceof BlazeMixingRecipe bmRecipe) fuelFluid = bmRecipe.getFuelFluid();
        else {
            long calculatedCost = BlazeMixingRecipe.durationToFuelCost(recipe.getProcessingDuration());
            fuelFluid = FluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, calculatedCost);

            ResourceLocation id = recipe.getId();
            this.id = new ResourceLocation("emi",
                    "blazinghot/blaze_mixing/" + id.getNamespace() + "/" + id.getPath());
        }
        this.fuels = List.of(firstFluidOrEmpty(fuelFluid.getMatchingFluidStacks()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        if (!fuels.isEmpty() && !fuels.get(0).isEmpty()) {
            addSlot(widgets, fuels.get(0), 35, 31).appendTooltip(Component
                    .translatable("emi.blazinghot.tooltip.blaze_mixing.fuel")
                    .withStyle(ChatFormatting.DARK_GREEN));
        }
        super.addWidgets(widgets);

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE) {
            CreateEmiAnimations.addBlazeBurner(widgets,
                    widgets.getWidth() / 2 + 3,
                    55,
                    requiredHeat.visualizeAsBlazeBurner());
        }
        BlazingEmiAnimations.addBlazeMixer(widgets, widgets.getWidth() / 2 + 3, 40);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        List<EmiIngredient> inputs = new ArrayList<>(super.getInputs());
        inputs.addAll(fuels);
        return inputs;
    }
}
