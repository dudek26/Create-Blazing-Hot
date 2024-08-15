package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.content.fluids.MoltenMetal;
import com.dudko.blazinghot.multiloader.MultiFluids.Constants;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

import static com.dudko.blazinghot.content.fluids.MoltenMetal.*;
import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.*;
import static com.dudko.blazinghot.util.ListUtil.compactLists;

@SuppressWarnings("unused")
public class BlazingMixingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingMixingRecipeGen(PackOutput output) {
        super(output);
    }

    List<GeneratedRecipe> ALL_MELTING_RECIPES =
            compactLists(MoltenMetal.ALL_METALS.stream().map(this::melting).toList());

    GeneratedRecipe
            NETHER_COMPOUND =
            create("nether_compound",
                    b -> b
                            .require(netherFlora())
                            .require(coal())
                            .require(clay())
                            .require(netherrackDust())
                            .require(soulDust())
                            .output(BlazingItems.NETHER_COMPOUND, 2)),
            NETHERRACK_DUST =
                    create("netherrack_dust",
                            b -> b
                                    .require(cinderFlour())
                                    .require(stoneDust())
                                    .output(BlazingItems.NETHERRACK_DUST)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold",
                            b -> custom(b).blazinghot$requireMultiple(netherEssence(), 2)
                                    .blazinghot$convertMeltables()
                                    .blazinghot$finish()
                                    .require(moltenGold(),
                                            Constants.ROD.platformed())
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .duration(200)
                                    .output(BLAZE_GOLD.fluid().get(),
                                            Constants.ROD.platformed()));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;

    }

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration) {
        return create("melting/" + tag.location().getPath(),
                b -> custom(b)
                        .blazinghot$convertMeltables()
                        .blazinghot$finish()
                        .require(tag)
                        .duration(duration * 3)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount));
    }

    private List<GeneratedRecipe> melting(MoltenMetal material) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (MoltenMetal.Forms form : material.supportedForms) {
            if (!form.mechanicalMixerMeltable) continue;
            TagKey<Item> tag = form.internalTag(material);
            Fluid result = material.fluid().get();
            recipes.add(melting(tag, result, form.amount, form.processingTime));
        }
        return recipes;
    }

}
