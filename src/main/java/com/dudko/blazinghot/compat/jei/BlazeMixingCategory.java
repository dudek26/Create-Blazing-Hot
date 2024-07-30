package com.dudko.blazinghot.compat.jei;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlazeMixingCategory extends BasinCategory {

    private final AnimatedBlazeMixer mixer = new AnimatedBlazeMixer();
    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
    MixingType type;

    protected enum MixingType {
        MIXING("blaze_mixing"),
        AUTO_SHAPELESS("blaze_automatic_shapeless"),
        AUTO_BREWING("blaze_automatic_brewing");

        public final String name;

        MixingType(String name) {
            this.name = name;
        }
    }

    public static BlazeMixingCategory standard(Info<BasinRecipe> info) {
        return new BlazeMixingCategory(info, MixingType.MIXING);
    }

    public static BlazeMixingCategory autoShapeless(Info<BasinRecipe> info) {
        return new BlazeMixingCategory(info, MixingType.AUTO_SHAPELESS);
    }

    public static BlazeMixingCategory autoBrewing(Info<BasinRecipe> info) {
        return new BlazeMixingCategory(info, MixingType.AUTO_BREWING);
    }

    protected BlazeMixingCategory(Info<BasinRecipe> info, MixingType type) {
        super(info, type != MixingType.AUTO_SHAPELESS);
        this.type = type;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull BasinRecipe recipe, @NotNull IFocusGroup focuses) {
        super.setRecipe(builder, recipe, focuses);
        FluidIngredient fuelFluid;
        if (recipe instanceof BlazeMixingRecipe bmRecipe) fuelFluid = bmRecipe.getFuelFluid();
        else {
            long calculatedCost = BlazeMixingRecipe.durationToFuelCost(recipe.getProcessingDuration());
            fuelFluid = FluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, calculatedCost);
        }

        List<FluidStack> fuels = new ArrayList<>(fuelFluid.getMatchingFluidStacks());

        if (!fuels.isEmpty() && !fuels.get(0).isEmpty()) builder
                .addSlot(RecipeIngredientRole.INPUT, 36, 32)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(FabricTypes.FLUID_STACK, toJei(withImprovedVisibility(fuels)))
                .addTooltipCallback(addFluidTooltip(fuelFluid.getRequiredAmount()))
                .addTooltipCallback((v, t) -> t.add(Component
                        .translatable("blazinghot.tooltip.blaze_mixing.fuel")
                        .withStyle(ChatFormatting.DARK_GREEN)));
    }

    @Override
    public void draw(@NotNull BasinRecipe recipe, @NotNull IRecipeSlotsView iRecipeSlotsView, @NotNull GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE) heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(graphics,
                getBackground().getWidth() / 2 + 3,
                55);
        mixer.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("blazinghot.recipe." + type.name);
    }
}
