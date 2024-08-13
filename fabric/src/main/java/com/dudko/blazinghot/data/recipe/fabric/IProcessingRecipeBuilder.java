package com.dudko.blazinghot.data.recipe.fabric;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

public interface IProcessingRecipeBuilder<T extends ProcessingRecipe<?>> {

    default IProcessingRecipeBuilder<T> blazinghot$requireFuel(TagKey<Fluid> fluidTag, long amount) {
        return blazinghot$requireFuel(FluidIngredient.fromTag(fluidTag, amount));
    }

    IProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidIngredient ingredient);

    default IProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidStack fluidStack) {
        return blazinghot$requireFuel(FluidIngredient.fromFluidStack(fluidStack));
    }

    IProcessingRecipeBuilder<T> blazinghot$convertMeltables();

    IProcessingRecipeBuilder<T> blazinghot$requireMultiple(Ingredient ingredient, int count);

    default IProcessingRecipeBuilder<T> blazinghot$requireMultiple(ItemLike item, int count) {
        return blazinghot$requireMultiple(Ingredient.of(item), count);
    }

    default IProcessingRecipeBuilder<T> blazinghot$requireMultiple(TagKey<Item> tag, int count) {
        return blazinghot$requireMultiple(Ingredient.of(tag), count);
    }

    @SuppressWarnings("unchecked")
    default ProcessingRecipeBuilder<T> blazinghot$finish() {
        return (ProcessingRecipeBuilder<T>) this;
    }

}
