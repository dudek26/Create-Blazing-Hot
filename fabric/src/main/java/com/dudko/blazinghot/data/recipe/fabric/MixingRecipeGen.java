package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.content.metal.Forms;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.multiloader.MultiFluids.Constants;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.*;
import static com.dudko.blazinghot.util.ListUtil.compactLists;

@SuppressWarnings("unused")
public class MixingRecipeGen extends BlazingProcessingRecipeGen {

    public MixingRecipeGen(PackOutput output) {
        super(output);
    }

    List<GeneratedRecipe>
            ALL_MELTING_RECIPES =
            compactLists(MoltenMetals.ALL.stream().filter(m -> m.mechanicalMixerMeltable).map(this::melting).toList());

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
                            b -> b.require(cinderFlour()).require(stoneDust()).output(BlazingItems.NETHERRACK_DUST)),
            MOLTEN_BLAZE_GOLD =
                    create("molten_blaze_gold",
                            b -> b
                                    .requireMultiple(netherEssence(), 2)
                                    .convertMeltable()
                                    .require(moltenGold(), Constants.ROD.platformed())
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .duration(200)
                                    .output(MoltenMetals.BLAZE_GOLD.fluid().get(), Constants.ROD.platformed())),
            MOLTEN_NETHERITE =
                    create("molten_netherite",
                            b -> b
                                    .convertMeltable()
                                    .require(moltenGold(), Forms.INGOT.amount)
                                    .require(moltenAncientDebris(), Forms.INGOT.amount)
                                    .duration(200)
                                    .requiresHeat(HeatCondition.SUPERHEATED)
                                    .output(BlazingFluidsImpl.MOLTEN_METALS.getFluid(MoltenMetals.NETHERITE),
                                            Forms.INGOT.amount / 4)),
            MOLTEN_ANDESITE =
                    create("molten_andesite",
                            b -> b
                                    .convertMeltable()
                                    .require(moltenIron(), Forms.NUGGET.amount)
                                    .require(andesite())
                                    .requiresHeat(HeatCondition.HEATED)
                                    .output(BlazingFluidsImpl.MOLTEN_METALS.getFluid(MoltenMetals.ANDESITE),
                                            Forms.ROD.amount * 3)),
            MOLTEN_BRASS =
                    create("molten_brass",
                            b -> b
                                    .convertMeltable()
                                    .require(moltenCopper(), Forms.INGOT.amount)
                                    .require(moltenZinc(), Forms.INGOT.amount)
                                    .requiresHeat(HeatCondition.HEATED)
                                    .output(BlazingFluidsImpl.MOLTEN_METALS.getFluid(MoltenMetals.BRASS),
                                            Forms.INGOT.amount * 2));

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;

    }

    private GeneratedRecipe melting(String name, Ingredient ingredient, Fluid result, long amount, int duration) {
        return create("melting/" + name,
                (b) -> b
                        .convertMeltable()
                        .require(ingredient)
                        .duration(duration)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(result, amount));
    }

    private GeneratedRecipe melting(ResourceLocation itemLocation, Fluid result, long amount, int duration) {
        return melting(itemLocation.getPath(),
                Ingredient.of(MultiRegistries.getItemFromRegistry(itemLocation).get()),
                result,
                amount,
                duration);
    }

    private GeneratedRecipe melting(TagKey<Item> tag, Fluid result, long amount, int duration) {
        return melting(tag.location().getPath(), Ingredient.of(tag), result, amount, duration);
    }

    private List<GeneratedRecipe> melting(MoltenMetal metal) {
        List<GeneratedRecipe> recipes = new ArrayList<>();
        metal
                .supportedForms()
                .stream()
                .filter(f -> f.mechanicalMixerMeltable)
                .forEach(form -> recipes.add(melting(form.internalTag(metal),
                        metal.fluid().get(),
                        form.amount,
                        form.processingTime)));
        metal
                .customForms()
                .stream()
                .filter(f -> f.mechanicalMixerMeltable)
                .forEach(form -> recipes.add(melting(form.customLocation,
                        metal.fluid().get(),
                        form.amount,
                        form.processingTime)));
        return recipes;
    }

}
