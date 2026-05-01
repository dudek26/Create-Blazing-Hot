package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidIngredient;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeMixingRecipeBuilder<R extends AbstractBlazeMixingRecipe> extends BlazingRecipeBuilder<ProcessingRecipeParams, R, BlazeMixingRecipeBuilder<R>> {

	protected SizedFluidIngredient mixerFuel;

	public BlazeMixingRecipeBuilder(ProcessingRecipe.Factory<ProcessingRecipeParams, R> factory, ResourceLocation recipeId) {
		super(factory, recipeId);
		mixerFuel = BlazeMixingRecipe.mixerFuelPlaceholder();
	}

	@Override
	protected BlazeMixingRecipeParams createParams() {
		return new BlazeMixingRecipeParams();
	}

	@Override
	public BlazeMixingRecipeBuilder<R> self() {
		return this;
	}

	@Override
	public void build(RecipeOutput consumer) {
		require(mixerFuel);
		super.build(consumer);
	}

	public BlazeMixingRecipeBuilder<R> mixerFuel(SizedFluidIngredient mixerFuel) {
		this.mixerFuel = mixerFuel;
		return self();
	}

	public BlazeMixingRecipeBuilder<R> mixerFuel(Fluid fluid, MultiAmount amount) {
		return mixerFuel(MultiFluidIngredient.fromFluid(BuiltInRegistries.FLUID.wrapAsHolder(fluid), amount));
	}

	public BlazeMixingRecipeBuilder<R> mixerFuel(TagKey<Fluid> fluidTag, MultiAmount amount) {
		return mixerFuel(MultiFluidIngredient.fromTag(fluidTag, amount));
	}

}
