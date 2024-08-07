package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class BlazingItemApplicationRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingItemApplicationRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            BLAZE_CASING =
            create("blaze_casing_from_log",
                    b -> b
                            .require(AllBlocks.COPPER_CASING)
                            .require(CommonTags.Items.BLAZE_GOLD_PLATES.internal)
                            .output(BlazingBlocks.BLAZE_CASING));


    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }
}
