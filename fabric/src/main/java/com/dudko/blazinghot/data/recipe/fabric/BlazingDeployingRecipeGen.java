package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.fluids.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import static com.dudko.blazinghot.content.fluids.MoltenMetal.*;

@SuppressWarnings("unused")
public class BlazingDeployingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingDeployingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            STELLAR_GOLDEN_APPLE = stellarApple(Items.GOLDEN_APPLE, BlazingItems.STELLAR_GOLDEN_APPLE),
            STELLAR_BLAZE_APPLE = stellarApple(BlazingItems.BLAZE_APPLE, BlazingItems.STELLAR_BLAZE_APPLE),
            STELLAR_IRON_APPLE = stellarApple(BlazingItems.IRON_APPLE, BlazingItems.STELLAR_IRON_APPLE);

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.DEPLOYING;
    }

    private GeneratedRecipe stellarApple(ItemLike metalApple, ItemLike result) {
        return create(result.asItem().toString(),
                b -> b
                        .require(metalApple)
                        .require(Items.NETHER_STAR)
                        .output(result));
    }

}
