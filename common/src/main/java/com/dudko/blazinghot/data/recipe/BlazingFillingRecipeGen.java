package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class BlazingFillingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingFillingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            GOLDEN_APPLE =
            create("golden_apple",
                   b -> b
                           .require(Items.APPLE)
                           .require(CommonTags.Fluids.MOLTEN_GOLD.internal, INGOT_COVER)
                           .output(Items.GOLDEN_APPLE)),
            GOLDEN_CARROT =
                    create("golden_carrot",
                           b -> b
                                   .require(Items.CARROT)
                                   .require(CommonTags.Fluids.MOLTEN_GOLD.internal, NUGGET_COVER)
                                   .output(Items.GOLDEN_CARROT)),
            GLISTERING_MELON =
                    create("glistering_melon",
                           b -> b
                                   .require(Items.MELON_SLICE)
                                   .require(CommonTags.Fluids.MOLTEN_GOLD.internal, NUGGET_COVER)
                                   .output(Items.GLISTERING_MELON_SLICE)),
            BLAZE_CARROT =
                    create("blaze_carrot",
                           b -> b
                                   .require(Items.CARROT)
                                   .require(CommonTags.Fluids.MOLTEN_BLAZE_GOLD.internal, NUGGET_COVER)
                                   .output(BlazingItems.BLAZE_CARROT)),
            BLAZE_APPLE =
                    create("blaze_apple",
                           b -> b
                                   .require(Items.APPLE)
                                   .require(CommonTags.Fluids.MOLTEN_BLAZE_GOLD.internal, INGOT_COVER)
                                   .output(BlazingItems.BLAZE_APPLE)),
            IRON_CARROT =
                    create("iron_carrot",
                           b -> b
                                   .require(Items.CARROT)
                                   .require(CommonTags.Fluids.MOLTEN_IRON.internal, NUGGET_COVER)
                                   .output(BlazingItems.IRON_CARROT)),
            IRON_APPLE =
                    create("iron_apple",
                           b -> b
                                   .require(Items.APPLE)
                                   .require(CommonTags.Fluids.MOLTEN_IRON.internal, INGOT_COVER)
                                   .output(BlazingItems.IRON_APPLE));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.FILLING;
    }
}
