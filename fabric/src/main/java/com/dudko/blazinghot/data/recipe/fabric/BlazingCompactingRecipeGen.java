package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.multiloader.MultiFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import static com.dudko.blazinghot.content.fluids.MoltenMetal.ALL_METALS;
import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.*;

@SuppressWarnings("unused")
public class BlazingCompactingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCompactingRecipeGen(PackOutput output) {
        super(output);
    }

    List<GeneratedRecipe> ALL_MOLTEN_COMPACTING_RECIPES = ALL_METALS.stream()
            .map(metal -> moltenToIngot(metal.moltenName(), metal.fluidTag(), metal.ingot().get()))
            .toList();

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

    private GeneratedRecipe moltenToIngot(String name, TagKey<Fluid> tag, ItemLike result) {
        return create(name,
                b -> custom(b).blazinghot$convertMeltables()
                        .blazinghot$finish()
                        .require(tag, MultiFluids.Constants.INGOT.platformed())
                        .output(result));
    }
}
