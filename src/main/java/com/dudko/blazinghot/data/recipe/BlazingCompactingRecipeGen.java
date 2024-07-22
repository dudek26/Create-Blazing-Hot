package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;

public class BlazingCompactingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCompactingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe
            MOLTEN_GOLD =
            create("molten_gold", b -> b.require(BlazingFluids.MOLTEN_GOLD.get(), INGOT).output(Items.GOLD_INGOT)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold", b -> b
                            .require(BlazingFluids.MOLTEN_BLAZE_GOLD.get(), INGOT)
                            .output(BlazingItems.BLAZE_GOLD_INGOT));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }
}
