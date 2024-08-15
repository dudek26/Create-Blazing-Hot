package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.content.fluids.MoltenMetal;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

import static com.dudko.blazinghot.util.ListUtil.compactLists;


@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class BlazeMixingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazeMixingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return BlazingRecipeTypes.BLAZE_MIXING.get();
    }

    List<GeneratedRecipe> ALL_MELTING_RECIPES =
            compactLists(MoltenMetal.ALL_METALS.stream().map(this::melting).toList());

    GeneratedRecipe
            NETHER_LAVA =
            create("nether_lava",
                    b -> custom(b).blazinghot$requireMultiple(BlazingItems.NETHER_ESSENCE, 2)
                            .blazinghot$finish()
                            .require(FluidIngredient.fromFluid(Fluids.LAVA, FluidConstants.BLOCK / 10))
                            .requiresHeat(HeatCondition.SUPERHEATED)
                            .output(BlazingFluidsImpl.NETHER_LAVA.get(), FluidConstants.BLOCK / 10)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold",
                            b -> custom(b).blazinghot$requireMultiple(BlazingItems.NETHER_ESSENCE, 2)
                                    .blazinghot$requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag,
                                            MultiFluids.fromBucketFraction(1, 20))
                                    .blazinghot$convertMeltables()
                                    .blazinghot$finish()
                                    .require(CommonTags.Fluids.MOLTEN_GOLD.internal,
                                            MultiFluids.Constants.INGOT.platformed())
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .duration(200)
                                    .output(BlazingFluidsImpl.MOLTEN_METALS.getFluid(MoltenMetal.BLAZE_GOLD),
                                            MultiFluids.Constants.INGOT.platformed()));

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration, long fuelCost) {
        return create("melting/" + tag.location().getPath(),
                (b) -> custom(b)
                        .blazinghot$requireFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, fuelCost)
                        .blazinghot$convertMeltables()
                        .blazinghot$finish()
                        .require(tag)
                        .duration(duration)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount));
    }

    private List<GeneratedRecipe> melting(MoltenMetal metal) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        for (MoltenMetal.Forms form : metal.supportedForms) {
            TagKey<Item> tag = form.internalTag(metal);
            Fluid result = metal.fluid().get();
            recipes.add(melting(tag, result, form.amount, form.processingTime, form.fuelCost));
        }
        return recipes;
    }

}

