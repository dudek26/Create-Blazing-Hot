package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

import static com.dudko.blazinghot.registry.BlazingItems.BLAZE_GOLD_INGOT;

@SuppressWarnings("unused")
public class BlazingPressingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingPressingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            BLAZE_GOLD =
            create("blaze_gold_ingot", b -> b.require(BLAZE_GOLD_INGOT).output(BlazingItems.BLAZE_GOLD_SHEET.get()));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
