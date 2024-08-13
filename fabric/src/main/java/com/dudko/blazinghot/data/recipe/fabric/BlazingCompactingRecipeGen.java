package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.multiloader.MultiFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class BlazingCompactingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCompactingRecipeGen(PackOutput output) {
        super(output);
    }

    List<GeneratedRecipe> ALL_MOLTEN_COMPACTING_RECIPES =
            Arrays.stream(Meltables.values()).map(m -> moltenToIngot(m.name, m.fluidTag, m.item)).toList();

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

    private GeneratedRecipe moltenToIngot(String materialName, TagKey<Fluid> tag, ItemLike result) {
        return create("molten_" + materialName,
                b -> custom(b).blazinghot$convertMeltables()
                        .blazinghot$finish()
                        .require(tag, MultiFluids.Constants.INGOT.platformed())
                        .output(result));
    }
}
