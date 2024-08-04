package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class BlazingDeployingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingDeployingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            STELLAR_GOLDEN_APPLE =
            create("stellar_golden_apple",
                   b -> b
                           .require(Items.GOLDEN_APPLE)
                           .require(Items.NETHER_STAR)
                           .output(BlazingItems.STELLAR_GOLDEN_APPLE)),
            STELLAR_BLAZE_APPLE =
                    create("stellar_blaze_apple",
                           b -> b
                                   .require(BlazingItems.BLAZE_APPLE)
                                   .require(Items.NETHER_STAR)
                                   .output(BlazingItems.STELLAR_BLAZE_APPLE)),
            STELLAR_IRON_APPLE =
                    create("stellar_iron_apple",
                           b -> b
                                   .require(BlazingItems.IRON_APPLE)
                                   .require(Items.NETHER_STAR)
                                   .output(BlazingItems.STELLAR_IRON_APPLE));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.DEPLOYING;
    }
}
