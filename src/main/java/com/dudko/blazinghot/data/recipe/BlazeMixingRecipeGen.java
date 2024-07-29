package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.Forms;
import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.Meltables;
import com.dudko.blazinghot.registry.BlazingFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

import static com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen.INGOT;

@SuppressWarnings("UnstableApiUsage")
public class BlazeMixingRecipeGen extends AbstractBlazeMixingRecipeGen {

    public BlazeMixingRecipeGen(PackOutput output) {
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

    GeneratedRecipe NETHER_LAVA = create("nether_lava", b -> requireMultiple(b, BlazingItems.NETHER_ESSENCE, 4)
            .require(FluidIngredient.fromFluid(Fluids.LAVA, FluidConstants.BLOCK / 10))
            .output(BlazingFluids.NETHER_LAVA.getSource(), FluidConstants.BLOCK / 10)), MOLTEN_BLAZE_GOLD = create(
            "molten_blaze_gold",
            b -> requireMultiple(b, BlazingItems.NETHER_ESSENCE, 4)
                    .requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag,
                            FluidConstants.BLOCK / 20)
                    .require(BlazingTags.Fluids.MOLTEN_GOLD.tag, INGOT)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(BlazingFluids.MOLTEN_BLAZE_GOLD.get(), INGOT));

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

    private static BlazeMixingRecipeBuilder requireMultiple(BlazeMixingRecipeBuilder builder, Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            builder.require(ingredient);
        }
        return builder;
    }

    private static BlazeMixingRecipeBuilder requireMultiple(BlazeMixingRecipeBuilder builder, ItemLike item, int count) {
        return requireMultiple(builder, Ingredient.of(item), count);
    }

    private static BlazeMixingRecipeBuilder requireMultiple(BlazeMixingRecipeBuilder builder, TagKey<Item> tag, int count) {
        return requireMultiple(builder, Ingredient.of(tag), count);
    }
}
