package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;

public class BlazingDeployingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingDeployingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe
            STELLAR_GOLDEN_APPLE =
            create("stellar_golden_apple", b -> b
                    .require(Items.GOLDEN_APPLE)
                    .require(Items.NETHER_STAR)
                    .output(BlazingItems.STELLAR_GOLDEN_APPLE)),
            STELLAR_BLAZE_APPLE =
                    create("stellar_blaze_apple", b -> b
                            .require(BlazingItems.BLAZE_APPLE)
                            .require(Items.NETHER_STAR)
                            .output(BlazingItems.STELLAR_BLAZE_APPLE));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.DEPLOYING;
    }
}
