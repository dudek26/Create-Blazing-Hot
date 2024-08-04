package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class BlazingCuttingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCuttingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe BLAZE_GOLD_SHEET = create("blaze_gold_sheet",
            b -> b.require(CommonTags.Items.BLAZE_GOLD_PLATES.internal).output(BlazingItems.BLAZE_GOLD_ROD, 2));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }
}
