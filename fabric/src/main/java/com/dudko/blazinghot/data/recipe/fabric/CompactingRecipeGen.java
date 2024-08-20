package com.dudko.blazinghot.data.recipe.fabric;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

import java.util.List;

import static com.dudko.blazinghot.content.metal.MoltenMetals.ALL;

@SuppressWarnings("unused")
public class CompactingRecipeGen extends BlazingProcessingRecipeGen {

    public CompactingRecipeGen(PackOutput output) {
        super(output);
    }

    List<GeneratedRecipe> ALL_MOLTEN_COMPACTING_RECIPES = ALL.stream()
            .map(metal -> create(metal.moltenName(),
                    b -> b.convertMeltable()
                            .require(metal.fluidTag(), metal.compactingResult().getSecond())
                            .output(metal.compactingResult().getFirst())))
            .toList();

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

}
