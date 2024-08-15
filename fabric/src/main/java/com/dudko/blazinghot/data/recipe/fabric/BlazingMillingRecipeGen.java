package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.*;

@SuppressWarnings("unused")
public class BlazingMillingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingMillingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            SOUL_SAND =
            create("soul_sand",
                    b -> b
                            .require(soulSand())
                            .output(BlazingItems.SOUL_DUST)
                            .output(0.5F, BlazingItems.SOUL_DUST)),
            STONE =
                    create("stone",
                            b -> b
                                    .require(stone())
                                    .output(BlazingItems.STONE_DUST)
                                    .output(0.5F, BlazingItems.STONE_DUST));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MILLING;
    }


}
