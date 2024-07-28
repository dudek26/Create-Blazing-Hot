package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;

public class BlazeMixingRecipeGen extends AbstractBlazeMixingRecipeGen{

    public BlazeMixingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe BLAZE_ROD = create("blaze_rod",
            b ->  b
                    .require(Items.BLAZE_POWDER)
                    .require(Items.GOLD_INGOT)
                    .requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, 16200)
                    .output(BlazingItems.BLAZE_GOLD_INGOT));
}
