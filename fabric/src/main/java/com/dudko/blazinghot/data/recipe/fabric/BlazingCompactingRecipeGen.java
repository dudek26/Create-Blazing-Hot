package com.dudko.blazinghot.data.recipe.fabric;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

import java.util.List;

import static com.dudko.blazinghot.content.metal.MoltenMetal.ALL_METALS;

@SuppressWarnings("unused")
public class BlazingCompactingRecipeGen extends BlazingProcessingRecipeGen {

    public BlazingCompactingRecipeGen(PackOutput output) {
        super(output);
    }

    List<GeneratedRecipe> ALL_MOLTEN_COMPACTING_RECIPES = ALL_METALS.stream()
            .map(metal -> create(metal.moltenName(),
                    b -> custom(b).blazinghot$convertMeltables()
                            .blazinghot$finish()
                            .require(metal.fluidTag(), metal.compactingResult().getSecond())
                            .output(metal.compactingResult().getFirst())))
            .toList();

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

}
