package com.dudko.blazinghot.compat.rei;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.compat.rei.category.BasinCategory;
import com.simibubi.create.compat.rei.category.CreateRecipeCategory;
import com.simibubi.create.compat.rei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.util.EntryIngredients;
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

//    @Override
//    public void addWidgets(CreateDisplay<BasinRecipe> display, List<Widget> widgets, Point origin) {
//
//        super.addWidgets(display, widgets, origin);
//
//        FluidIngredient fuelFluid;
//        BasinRecipe recipe = display.getRecipe();
//
//        if (recipe instanceof BlazeMixingRecipe bmRecipe) fuelFluid = bmRecipe.getFuelFluid();
//        else {
//            long calculatedCost = BlazeMixingRecipe.durationToFuelCost(recipe.getProcessingDuration());
//            fuelFluid = FluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, calculatedCost);
//        }
//
//        List<FluidStack> fuels = new ArrayList<>(fuelFluid.getMatchingFluidStacks());
//
//        Slot fuelSlot = basicSlot(36, 32)
//                .markInput()
//                .entries(EntryIngredients.of(CreateRecipeCategory.convertToREIFluid(fuels.get(0))));
//        CreateRecipeCategory.setFluidRenderRatio(fuelSlot);
//        widgets.add(fuelSlot);
//    }

    @Override
    public void draw(BasinRecipe recipe, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, graphics, mouseX, mouseY);
        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE) heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(graphics,
                getDisplayWidth(null) / 2 + 3,
                55);
        mixer.draw(graphics, getDisplayWidth(null) / 2 + 3, 34);
    }

}
