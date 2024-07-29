package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.Forms;
import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.Meltables;
import com.dudko.blazinghot.registry.BlazingFluids;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

import static com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.INGOT;

public class BlazeMixingRecipeGen extends AbstractBlazeMixingRecipeGen {

    public BlazeMixingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    GeneratedRecipe BLAZE_GOLD_ROD_MELTING = melting(BlazingTags.Items.BLAZE_GOLD_RODS.tag,
            BlazingFluids.MOLTEN_BLAZE_GOLD.get(),
            INGOT / 2,
            300,
            BlazeMixingRecipe.durationToFuelCost(300));
    List<GeneratedRecipe> BLAZE_GOLD_MELTING = meltingAll(Meltables.BLAZE_GOLD, BlazingFluids.MOLTEN_BLAZE_GOLD.get());
    List<GeneratedRecipe> IRON_MELTING = meltingAll(Meltables.IRON, BlazingFluids.MOLTEN_IRON.get());
    List<GeneratedRecipe> GOLD_MELTING = meltingAll(Meltables.GOLD, BlazingFluids.MOLTEN_GOLD.get());

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration, long fuelCost) {
        return create("melting/" + tag.location().getPath(), b -> b
                .require(tag)
                .duration(duration)
                .requiresHeat(HeatCondition.SUPERHEATED)
                .output(result, amount)
                .requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, fuelCost));
    }

    private GeneratedRecipe melting(ItemLike item, Fluid result, long amount, int duration, long fuelCost) {
        return create("melting/" + item.asItem(),
                b -> b
                        .require(item)
                        .duration(duration)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount)
                        .requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, fuelCost));
    }

    private List<GeneratedRecipe> meltingAll(Meltables material, Fluid result) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (Forms form : Forms.values()) {
            recipes.add(melting(form.tag(material), result, form.amount, form.meltingTime, form.fuelCost));
        }
        return recipes;
    }
}
