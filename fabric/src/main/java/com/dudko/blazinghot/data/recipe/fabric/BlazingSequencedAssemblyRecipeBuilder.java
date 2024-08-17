package com.dudko.blazinghot.data.recipe.fabric;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class BlazingSequencedAssemblyRecipeBuilder extends SequencedAssemblyRecipeBuilder {

    private final SequencedAssemblyRecipe recipe;

    public BlazingSequencedAssemblyRecipeBuilder(ResourceLocation id) {
        super(id);
        this.recipe = new SequencedAssemblyRecipe(id,
                AllRecipeTypes.SEQUENCED_ASSEMBLY.getSerializer());
    }

    public <T extends ProcessingRecipe<?>> SequencedAssemblyRecipeBuilder addCustomStep(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory,
                                                                                  Function<BlazingProcessingRecipeBuilder<T>, ProcessingRecipeBuilder<T>> builder) {
        BlazingProcessingRecipeBuilder<T> recipeBuilder =
                new BlazingProcessingRecipeBuilder<>(factory, new ResourceLocation("dummy"));
        Item placeHolder = recipe.getTransitionalItem()
                .getItem();
        recipe.getSequence()
                .add(new SequencedRecipe<>(builder.apply((BlazingProcessingRecipeBuilder<T>) recipeBuilder.require(placeHolder)
                                .output(placeHolder))
                        .build()));
        return this;
    }

}
