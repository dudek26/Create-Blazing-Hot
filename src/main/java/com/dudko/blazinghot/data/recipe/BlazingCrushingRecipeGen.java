package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BlazingCrushingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCrushingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe
            STONE =
            create("stone", b -> b
                    .require(Blocks.STONE)
                    .output(BlazingItems.STONE_DUST, 2)
                    .output(0.6F, BlazingItems.STONE_DUST)
                    .output(0.4F, BlazingItems.STONE_DUST)
                    .duration(250)),
            SOUL_SAND =
                    create("soul_sand", b -> b
                            .require(Blocks.SOUL_SAND)
                            .output(BlazingItems.SOUL_DUST, 2)
                            .output(0.6F, BlazingItems.SOUL_DUST)
                            .output(0.4F, BlazingItems.SOUL_DUST)
                            .duration(200)),
            BLAZE_GOLD_ROD =
                    create("blaze_gold_rod", b -> b
                            .require(BlazingItems.BLAZE_GOLD_ROD)
                            .output(0.3F, Items.BLAZE_POWDER)
                            .duration(250));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CRUSHING;
    }
}
