package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingFluids.MultiloaderFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.util.FluidUtil;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
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


@SuppressWarnings({"UnstableApiUsage"})
public class BlazeMixingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazeMixingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return BlazingRecipeTypes.BLAZE_MIXING;
    }


    GeneratedRecipe
            BLAZE_GOLD_ROD_MELTING =
            melting(CommonTags.Items.BLAZE_GOLD_RODS.internal,
                    MultiloaderFluids.MOLTEN_BLAZE_GOLD.get(),
                    INGOT / 2,
                    300,
                    BlazeMixingRecipe.durationToFuelCost(300));
    List<GeneratedRecipe>
            BLAZE_GOLD_MELTING =
            meltingAll(Meltables.BLAZE_GOLD, MultiloaderFluids.MOLTEN_BLAZE_GOLD.get());
    List<GeneratedRecipe> IRON_MELTING = meltingAll(Meltables.IRON, MultiloaderFluids.MOLTEN_IRON.get());
    List<GeneratedRecipe> GOLD_MELTING = meltingAll(Meltables.GOLD, MultiloaderFluids.MOLTEN_GOLD.get());

    GeneratedRecipe
            NETHER_LAVA =
            create("nether_lava",
                    b -> requireMultiple(b, BlazingItems.NETHER_ESSENCE, 4)
                            .require(FluidIngredient.fromFluid(Fluids.LAVA, FluidUtil.BLOCK / 10))
                            .requiresHeat(HeatCondition.SUPERHEATED)
                            .output(MultiloaderFluids.NETHER_LAVA.get(), FluidConstants.BLOCK / 10)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold",
                            b -> ((IProcessingRecipeBuilder<ProcessingRecipe<?>>) requireMultiple(b,
                                    BlazingItems.NETHER_ESSENCE,
                                    4))
                                    .blazinghot$requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag,
                                            FluidConstants.BLOCK / 20)
                                    .require(CommonTags.Fluids.MOLTEN_GOLD.internal, INGOT)
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .output(MultiloaderFluids.MOLTEN_BLAZE_GOLD.get(), INGOT));

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration, long fuelCost) {
        return create("melting/" + tag.location().getPath(),
                (b) -> ((IProcessingRecipeBuilder<ProcessingRecipe<?>>) b)
                        .blazinghot$requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, fuelCost)
                        .require(tag)
                        .duration(duration)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount));
    }

    private GeneratedRecipe melting(ItemLike item, Fluid result, long amount, int duration, long fuelCost) {
        return create("melting/" + item.asItem(),
                b -> ((IProcessingRecipeBuilder<ProcessingRecipe<?>>) b)
                        .blazinghot$requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, fuelCost)
                        .require(item)
                        .duration(duration)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount));
    }

    private List<GeneratedRecipe> meltingAll(Meltables material, Fluid result) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (Forms form : Forms.values()) {
            recipes.add(melting(form.tag(material), result, form.amount, form.meltingTime, form.fuelCost));
        }
        return recipes;
    }

    private static ProcessingRecipeBuilder<ProcessingRecipe<?>> requireMultiple(ProcessingRecipeBuilder<ProcessingRecipe<?>> builder, Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            builder.require(ingredient);
        }
        return builder;
    }

    private static ProcessingRecipeBuilder<ProcessingRecipe<?>> requireMultiple(ProcessingRecipeBuilder<ProcessingRecipe<?>> builder, ItemLike item, int count) {
        return requireMultiple(builder, Ingredient.of(item), count);
    }

    private static ProcessingRecipeBuilder<ProcessingRecipe<?>> requireMultiple(ProcessingRecipeBuilder<ProcessingRecipe<?>> builder, TagKey<Item> tag, int count) {
        return requireMultiple(builder, Ingredient.of(tag), count);
    }


}

