package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;

public class BlazingMillingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingMillingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe
            SOUL_SAND =
            create("soul_sand",
                   b -> b.require(Items.SOUL_SAND).output(BlazingItems.SOUL_DUST).output(0.5F, BlazingItems.SOUL_DUST)),
            STONE =
                    create("stone",
                           b -> b.require(Items.STONE).output(BlazingItems.STONE_DUST).output(0.5F, BlazingItems.STONE_DUST));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MILLING;
    }


}
