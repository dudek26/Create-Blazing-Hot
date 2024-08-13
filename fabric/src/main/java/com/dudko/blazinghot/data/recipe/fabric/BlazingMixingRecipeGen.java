package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.multiloader.MultiFluids;
import com.dudko.blazinghot.registry.BlazingFluids.MultiloaderFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BlazingMixingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingMixingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe
            NETHER_COMPOUND =
            create("nether_compound",
                    b -> b
                            .require(BlazingTags.Items.NETHER_FLORA.tag)
                            .require(ItemTags.COALS)
                            .require(Items.CLAY_BALL)
                            .require(BlazingItems.NETHERRACK_DUST)
                            .require(BlazingItems.SOUL_DUST)
                            .output(BlazingItems.NETHER_COMPOUND, 2)),
            NETHERRACK_DUST =
                    create("netherrack_dust",
                            b -> b
                                    .require(AllItems.CINDER_FLOUR)
                                    .require(BlazingItems.STONE_DUST)
                                    .output(BlazingItems.NETHERRACK_DUST)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold",
                            b -> custom(b).blazinghot$requireMultiple(BlazingItems.NETHER_ESSENCE, 2)
                                    .blazinghot$convertMeltables()
                                    .finish()
                                    .require(CommonTags.Fluids.MOLTEN_GOLD.internal,
                                            MultiFluids.Constants.ROD.platformed())
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .duration(200)
                                    .output(MultiloaderFluids.MOLTEN_BLAZE_GOLD.get(),
                                            MultiFluids.Constants.INGOT.platformed())),
            BLAZE_GOLD_ROD_MELTING =
                    melting(CommonTags.Items.BLAZE_GOLD_RODS.internal,
                            BlazingFluidsImpl.MOLTEN_BLAZE_GOLD.get(),
                            Forms.ROD.amount,
                            Forms.ROD.meltingTime);

    List<GeneratedRecipe> ALL_MELTING_RECIPES = meltingAll();

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;

    }

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration) {
        return create("melting/" + tag.location().getPath(),
                b -> custom(b)
                        .blazinghot$convertMeltables()
                        .finish()
                        .require(tag)
                        .duration(duration * 3)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount));
    }

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, Forms form) {
        return melting(tag, result, form.amount, form.meltingTime);
    }

    private List<GeneratedRecipe> melting(Meltables material) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (Forms form : Forms.values()) {
            if (!form.mechanicalMixerMelting) continue;
            if (form == Forms.ROD) continue;
            recipes.add(melting(form.tag(material), material.fluid, form));
        }
        return recipes;
    }

    private List<GeneratedRecipe> meltingAll() {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (Meltables material : Meltables.values())
            recipes.addAll(melting(material));
        return recipes;
    }


    private static <T extends ProcessingRecipe<?>> ProcessingRecipeBuilder<T> requireMultiple(ProcessingRecipeBuilder<T> builder, Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            builder.require(ingredient);
        }
        return builder;
    }

    private static <T extends ProcessingRecipe<?>> ProcessingRecipeBuilder<T> requireMultiple(ProcessingRecipeBuilder<T> builder, ItemLike item, int count) {
        return requireMultiple(builder, Ingredient.of(item), count);
    }

    private static <T extends ProcessingRecipe<?>> ProcessingRecipeBuilder<T> requireMultiple(ProcessingRecipeBuilder<T> builder, TagKey<Item> tag, int count) {
        return requireMultiple(builder, Ingredient.of(tag), count);
    }

}
