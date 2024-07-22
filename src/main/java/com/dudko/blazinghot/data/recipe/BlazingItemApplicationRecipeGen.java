package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class BlazingItemApplicationRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingItemApplicationRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe
            BLAZE_CASING =
            create("blaze_casing_from_log", b -> b
                    .require(AllBlocks.COPPER_CASING)
                    .require(BlazingTags.Items.BLAZE_GOLD_INGOTS.tag)
                    .output(BlazingBlocks.BLAZE_CASING));


    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }
}
