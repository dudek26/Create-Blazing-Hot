package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.*;

@SuppressWarnings("unused")
public class DeployingRecipeGen extends BlazingProcessingRecipeGen {

    public DeployingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            STELLAR_GOLDEN_APPLE = stellarApple(goldenApple(), BlazingItems.STELLAR_GOLDEN_APPLE),
            STELLAR_BLAZE_APPLE = stellarApple(blazeApple(), BlazingItems.STELLAR_BLAZE_APPLE),
            STELLAR_IRON_APPLE = stellarApple(ironApple(), BlazingItems.STELLAR_IRON_APPLE),
            STELLAR_ZINC_APPLE = stellarApple(zincApple(), BlazingItems.STELLAR_ZINC_APPLE),
            STELLAR_COPPER_APPLE = stellarApple(copperAppple(), BlazingItems.STELLAR_COPPER_APPLE),
            STELLAR_BRASS_APPLE = stellarApple(brassApple(), BlazingItems.STELLAR_BRASS_APPLE);

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
