package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.*;
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
                    b -> custom(b).blazinghot$requireMultiple(netherEssence(), 2)
                            .blazinghot$finish()
                            .require(lava(), FluidConstants.BLOCK / 10)
                            .requiresHeat(HeatCondition.SUPERHEATED)
                            .output(BlazingFluidsImpl.NETHER_LAVA.get(), FluidConstants.BLOCK / 10)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold",
                            b -> custom(b).blazinghot$requireMultiple(netherEssence(), 2)
                                    .blazinghot$requireFuel(fuel(),
                                            MultiFluids.fromBucketFraction(1, 20))
                                    .blazinghot$convertMeltables()
                                    .blazinghot$finish()
                                    .require(moltenGold(),
                                            MultiFluids.Constants.INGOT.platformed())
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .duration(200)
                                    .output(BlazingFluidsImpl.MOLTEN_METALS.getFluid(MoltenMetal.BLAZE_GOLD),
                                            MultiFluids.Constants.INGOT.platformed()));

    private GeneratedRecipe melting(String name, Ingredient ingredient, Fluid result, long amount, int duration, long fuelCost) {
        return create("melting/" + name,
                (b) -> custom(b)
                        .blazinghot$requireFuel(fuel(), fuelCost)
                        .blazinghot$convertMeltables()
                        .blazinghot$finish()
                        .require(ingredient)
                        .duration(duration)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount));
    }

    private GeneratedRecipe melting(ResourceLocation itemLocation, Fluid result, long amount, int duration, long fuelCost) {
        return melting(itemLocation.getPath(),
                Ingredient.of(MultiRegistries.getItemFromRegistry(itemLocation).get()),
                result,
                amount,
                duration,
                fuelCost);
    }

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration, long fuelCost) {
        return melting(tag.location().getPath(),
                Ingredient.of(tag),
                result,
                amount,
                duration,
                fuelCost);
    }

    private List<GeneratedRecipe> melting(MoltenMetal metal) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        metal.supportedForms().forEach(form ->
                recipes.add(melting(form.internalTag(metal),
                        metal.fluid().get(),
                        form.amount,
                        form.processingTime,
                        form.fuelCost))
        );
        metal.customForms()
                .forEach(form -> recipes.add(melting(form.customLocation,
                        metal.fluid().get(),
                        form.amount,
                        form.processingTime,
                        form.fuelCost)));
        return recipes;
    }

}

