package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.mixin.fabric.SequencedAssemblyRecipeBuilderAccessor;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class BlazingSequencedAssemblyRecipeBuilder extends SequencedAssemblyRecipeBuilder {

    public BlazingSequencedAssemblyRecipeBuilder(ResourceLocation id) {
        super(id);
    }

    /**
     * Converts droplets to milibuckets on Forge not by the 81:1 ratio but by {@link MultiFluids#MELTABLE_CONVERSION}
     */
    public <T extends ProcessingRecipe<?>> BlazingSequencedAssemblyRecipeBuilder addMeltableStep(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory,
                                                                                                 Function<BlazingProcessingRecipeBuilder<T>, ProcessingRecipeBuilder<T>> builder) {
        SequencedAssemblyRecipeBuilderAccessor self = (SequencedAssemblyRecipeBuilderAccessor) this;
        BlazingProcessingRecipeBuilder<T> recipeBuilder =
                new BlazingProcessingRecipeBuilder<>(factory, new ResourceLocation("dummy"));
        Item placeHolder = self.getRecipe().getTransitionalItem()
                .getItem();
        self.getRecipe().getSequence()
                .add(new BlazingSequencedRecipe<>(builder.apply((BlazingProcessingRecipeBuilder<T>) recipeBuilder.require(
                                        placeHolder)
                                .output(placeHolder))
                        .build(), true));
        return this;
    }

    @Override
    public BlazingSequencedAssemblyRecipeBuilder require(TagKey<Item> tag) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.require(tag);
    }

    @Override
    public BlazingSequencedAssemblyRecipeBuilder require(ItemLike ingredient) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.require(ingredient);
    }

    @Override
    public BlazingSequencedAssemblyRecipeBuilder require(Ingredient ingredient) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.require(ingredient);
    }

    @Override
    public BlazingSequencedAssemblyRecipeBuilder transitionTo(ItemLike item) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.transitionTo(item);
    }

    @Override
    public BlazingSequencedAssemblyRecipeBuilder addOutput(ItemLike item, float weight) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.addOutput(item, weight);
    }

    @Override
    public BlazingSequencedAssemblyRecipeBuilder addOutput(ItemStack item, float weight) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.addOutput(item, weight);
    }

    @Override
    public BlazingSequencedAssemblyRecipeBuilder loops(int loops) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.loops(loops);
    }

    @Override
    public <T extends ProcessingRecipe<?>> BlazingSequencedAssemblyRecipeBuilder addStep(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, UnaryOperator<ProcessingRecipeBuilder<T>> builder) {
        return (BlazingSequencedAssemblyRecipeBuilder) super.addStep(factory, builder);
    }

}
