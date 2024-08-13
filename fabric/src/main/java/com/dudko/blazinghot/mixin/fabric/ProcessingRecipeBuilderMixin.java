package com.dudko.blazinghot.mixin.fabric;

import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipeBuilder;
import com.dudko.blazinghot.data.recipe.fabric.IProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ProcessingRecipeBuilder.class, remap = false)
public abstract class ProcessingRecipeBuilderMixin<T extends ProcessingRecipe<?>> implements IProcessingRecipeBuilder<T> {

    @Shadow
    protected ProcessingRecipeBuilder.ProcessingRecipeParams params;

    @Shadow public abstract ProcessingRecipeBuilder<T> require(Ingredient item);

    @Override
    public IProcessingRecipeBuilder<T> blazinghot$requireFuel(FluidIngredient fuel) {
        ((IProcessingRecipeParams) params).blazinghot$setFuelFluid(fuel);
        return this;
    }

    @Override
    public IProcessingRecipeBuilder<T> blazinghot$convertMeltables() {
        ((IProcessingRecipeParams) params).blazinghot$setFormConversion(true);
        return this;
    }

    @Override
    public IProcessingRecipeBuilder<T> blazinghot$requireMultiple(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) require(ingredient);
        return this;
    }

}
