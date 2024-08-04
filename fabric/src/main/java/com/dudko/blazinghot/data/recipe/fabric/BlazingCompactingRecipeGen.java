package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class BlazingCompactingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCompactingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            MOLTEN_GOLD =
            create("molten_gold",
                   b -> b.require(CommonTags.Fluids.MOLTEN_GOLD.internal, INGOT).output(Items.GOLD_INGOT)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold",
                           b -> b
                                   .require(CommonTags.Fluids.MOLTEN_BLAZE_GOLD.internal, INGOT)
                                   .output(BlazingItems.BLAZE_GOLD_INGOT)),
            MOLTEN_IRON =
                    create("molten_iron",
                           b -> b.require(CommonTags.Fluids.MOLTEN_IRON.internal, INGOT).output(Items.IRON_INGOT));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }
}
