package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import static com.dudko.blazinghot.content.metal.MoltenMetal.*;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.*;

@SuppressWarnings("unused")
public class FillingRecipeGen extends BlazingProcessingRecipeGen {

    public FillingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            GLISTERING_MELON =
            create("glistering_melon",
                    b -> b.convertMeltable()
                            .require(melon())
                            .require(moltenGold(), NUGGET_COVER)
                            .output(Items.GLISTERING_MELON_SLICE)),
            GOLDEN_APPLE = metalApple(GOLD, Items.GOLDEN_APPLE),
            GOLDEN_CARROT = metalCarrot(GOLD, Items.GOLDEN_CARROT),
            BLAZE_CARROT = metalCarrot(BLAZE_GOLD, BlazingItems.BLAZE_CARROT),
            BLAZE_APPLE = metalApple(BLAZE_GOLD, BlazingItems.BLAZE_APPLE),
            IRON_CARROT = metalCarrot(IRON, BlazingItems.IRON_CARROT),
            IRON_APPLE = metalApple(IRON, BlazingItems.IRON_APPLE);

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.FILLING;
    }

    private GeneratedRecipe metalApple(MoltenMetal metal, ItemLike result) {
        return create(result.asItem().toString(),
                b -> b.convertMeltable()
                        .require(apple())
                        .require(metal.fluidTag(), INGOT_COVER)
                        .output(result));
    }

    private GeneratedRecipe metalCarrot(MoltenMetal metal, ItemLike result) {
        return create(result.asItem().toString(),
                b -> b.convertMeltable()
                        .require(carrot())
                        .require(metal.fluidTag(), NUGGET_COVER)
                        .output(result));
    }
}
