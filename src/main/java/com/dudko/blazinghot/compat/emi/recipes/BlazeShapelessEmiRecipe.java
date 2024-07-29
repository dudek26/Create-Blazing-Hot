package com.dudko.blazinghot.compat.emi.recipes;

import com.simibubi.create.content.processing.basin.BasinRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import net.minecraft.resources.ResourceLocation;

public class BlazeShapelessEmiRecipe extends BlazeMixingEmiRecipe{

    public BlazeShapelessEmiRecipe(EmiRecipeCategory category, BasinRecipe recipe) {
        super(category, recipe);
        ResourceLocation id = recipe.getId();
        this.id = new ResourceLocation ("emi", "blazinghot/blaze_shapeless/" + id.getNamespace() + "/" + id.getPath());
    }

}
