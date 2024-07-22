package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public class BlazingMixingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingMixingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe NETHER_COMPOUND = create("nether_compound", b -> b
            .require(BlazingTags.Items.NETHER_FLORA.tag)
            .require(ItemTags.COALS)
            .require(Items.CLAY_BALL)
            .require(BlazingItems.NETHERRACK_DUST)
            .require(BlazingItems.SOUL_DUST)
            .output(BlazingItems.NETHER_COMPOUND, 2)), NETHERRACK_DUST = create("netherrack_dust", b -> b
            .require(AllItems.CINDER_FLOUR)
            .require(BlazingItems.STONE_DUST)
            .output(BlazingItems.NETHERRACK_DUST)), MOLTEN_BLAZE_GOLD = create("molten_blaze_gold", b -> b
            .require(BlazingTags.Fluids.MOLTEN_GOLD.tag, INGOT / 2)
            .require(BlazingItems.NETHER_ESSENCE)
            .require(BlazingItems.NETHER_ESSENCE)
            .require(BlazingItems.NETHER_ESSENCE)
            .require(BlazingItems.NETHER_ESSENCE)
            .output(BlazingFluids.MOLTEN_BLAZE_GOLD.get(), INGOT / 2));

    List<GeneratedRecipe> GOLD_MELTING = meltingAll("gold", BlazingFluids.MOLTEN_GOLD.get());
    List<GeneratedRecipe> IRON_MELTING = meltingAll("iron", BlazingFluids.MOLTEN_IRON.get());
    List<GeneratedRecipe> BLAZE_GOLD_MELTING = meltingAll("blaze_gold", BlazingFluids.MOLTEN_BLAZE_GOLD.get());

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;
    }

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration) {
        return create("melting/" + tag.location().getPath(),
                      b -> b.require(tag).duration(duration).output(result, amount).requiresHeat(HeatCondition.SUPERHEATED));
    }

    private List<GeneratedRecipe> meltingAll(String material, Fluid result) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (Forms form : Forms.values()) {
            recipes.add(create("melting/" + form.tag(material), b -> b
                    .require(BlazingTags.commonItemTag(form.tag(material)))
                    .duration(form.meltingTime)
                    .output(result, form.amount)));
        }
        return recipes;
    }

}
