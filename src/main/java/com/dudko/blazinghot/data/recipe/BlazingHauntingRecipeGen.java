package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class BlazingHauntingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingHauntingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe
            NETHER_COMPOUND =
            create("nether_compound", b -> b.require(BlazingItems.NETHER_COMPOUND).output(BlazingItems.NETHER_ESSENCE));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.HAUNTING;
    }
}
