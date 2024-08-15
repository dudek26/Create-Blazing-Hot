package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.*;

@SuppressWarnings("unused")
public class BlazingItemApplicationRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingItemApplicationRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            BLAZE_CASING =
            create("blaze_casing_from_log",
                    b -> b
                            .require(copperCasing())
                            .require(blazeGoldSheet())
                            .output(BlazingBlocks.BLAZE_CASING));


    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }
}
