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

import static com.dudko.blazinghot.content.fluids.MoltenMetal.BLAZE_GOLD;
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
                                    .blazinghot$finish()
                                    .require(CommonTags.Fluids.MOLTEN_GOLD.internal,
                                            Constants.ROD.platformed())
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .duration(200)
                                    .output(BlazingFluidsImpl.MOLTEN_METALS.getFluid(BLAZE_GOLD),
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
