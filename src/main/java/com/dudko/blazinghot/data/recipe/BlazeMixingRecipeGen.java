package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.Forms;
import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.Meltables;
import com.dudko.blazinghot.registry.BlazingFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

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

    List<GeneratedRecipe> BLAZE_GOLD_MELTING = meltingAll(Meltables.BLAZE_GOLD, BlazingFluids.MOLTEN_BLAZE_GOLD.get());

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration) {
        return create("melting/" + tag.location().getPath(),
                b -> b.require(tag).duration(duration).requiresHeat(HeatCondition.SUPERHEATED).output(result, amount));
    }

    private GeneratedRecipe melting(ItemLike item, Fluid result, long amount, int duration) {
        return create("melting/" + item.asItem(),
                b -> b.require(item).duration(duration).requiresHeat(HeatCondition.SUPERHEATED).output(result, amount));
    }

    private List<GeneratedRecipe> meltingAll(Meltables material, Fluid result) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (Forms form : Forms.values()) {
            melting(form.tag(material), result, form.amount, form.meltingTime);
        }
        return recipes;
    }
}
