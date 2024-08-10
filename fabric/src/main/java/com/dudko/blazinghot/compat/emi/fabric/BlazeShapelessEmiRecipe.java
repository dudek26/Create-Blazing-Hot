package com.dudko.blazinghot.compat.emi.fabric;

import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.getFuelCost;

public class BlazeShapelessEmiRecipe extends BlazeMixingEmiRecipe {

    public BlazeShapelessEmiRecipe(EmiRecipeCategory category, BasinRecipe recipe, long fuelCost) {
        super(category, recipe);
        ResourceLocation id = recipe.getId();
        this.id = new ResourceLocation("emi", "blazinghot/blaze_shapeless/" + id.getNamespace() + "/" + id.getPath());
        FluidIngredient fuel = FluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, fuelCost);
        this.fuels = List.of(firstFluidOrEmpty(fuel.getMatchingFluidStacks()));
    }

}
