package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class BlazingCuttingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCuttingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe
            BLAZE_GOLD_SHEET =
            create("blaze_gold_sheet",
                   b -> b.require(BlazingTags.Items.BLAZE_GOLD_PLATES.tag).output(BlazingItems.BLAZE_GOLD_ROD, 2));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }
}
