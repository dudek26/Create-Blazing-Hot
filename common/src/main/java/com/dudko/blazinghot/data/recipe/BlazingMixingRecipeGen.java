package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.registry.BlazingFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
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

    GeneratedRecipe NETHER_COMPOUND = create("nether_compound",
            b -> b
                    .require(BlazingTags.Items.NETHER_FLORA.tag)
                    .require(ItemTags.COALS)
                    .require(Items.CLAY_BALL)
                    .require(BlazingItems.NETHERRACK_DUST)
                    .require(BlazingItems.SOUL_DUST)
                    .output(BlazingItems.NETHER_COMPOUND, 2)),

    NETHERRACK_DUST = create("netherrack_dust",
            b -> b
                    .require(AllItems.CINDER_FLOUR)
                    .require(BlazingItems.STONE_DUST)
                    .output(BlazingItems.NETHERRACK_DUST));

//    MOLTEN_BLAZE_GOLD = create("molten_blaze_gold", todo
//            b -> requireMultiple(b, BlazingItems.NETHER_ESSENCE, 4)
//                    .require(BlazingTags.Fluids.MOLTEN_GOLD.tag,
//                            INGOT / 2)
//                    .requiresHeat(HeatCondition.SUPERHEATED)
//                    .output(BlazingFluids.MOLTEN_BLAZE_GOLD.get(), INGOT / 2));

//    List<GeneratedRecipe> GOLD_MELTING = meltingAll(Meltables.GOLD, BlazingFluids.MOLTEN_GOLD.get()); todo
//    List<GeneratedRecipe> IRON_MELTING = meltingAll(Meltables.IRON, BlazingFluids.MOLTEN_IRON.get());
//    List<GeneratedRecipe> BLAZE_GOLD_MELTING = meltingAll(Meltables.BLAZE_GOLD, BlazingFluids.MOLTEN_BLAZE_GOLD.get());

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;
    }

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration) {
        return create("melting/" + tag.location().getPath(), b -> b
                .require(tag)
                .duration(duration * 5)
                .requiresHeat(HeatCondition.SUPERHEATED)
                .output(result, amount));
    }

    /* todo
    private List<GeneratedRecipe> meltingAll(Meltables material, Fluid result) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (Forms form : Forms.values()) {
            if (form == Forms.BLOCK) continue;
            recipes.add(melting(form.tag(material), result, form.amount, form.meltingTime));
        }
        return recipes;
    }

     */

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
