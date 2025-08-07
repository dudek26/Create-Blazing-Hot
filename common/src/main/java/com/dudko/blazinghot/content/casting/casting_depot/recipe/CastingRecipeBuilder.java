package com.dudko.blazinghot.content.casting.casting_depot.recipe;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;

import net.minecraft.resources.ResourceLocation;

public class CastingRecipeBuilder extends BlazingRecipeBuilder<CastingRecipeParams, CastingRecipe, CastingRecipeBuilder> {

	public CastingRecipeBuilder(ProcessingRecipe.Factory<CastingRecipeParams, CastingRecipe> factory, ResourceLocation recipeId) {
		super(factory, recipeId);
	}

	@Override
	protected CastingRecipeParams createParams() {
		return new CastingRecipeParams();
	}

	@Override
	public CastingRecipeBuilder self() {
		return this;
	}

	public CastingRecipeBuilder coolingDuration(int ticks) {
		params.coolingDuration = ticks;
		return self();
	}

	public CastingRecipeBuilder castingDuration(long fluidAmount) {
		float coolingFactor = 3f;
		int baseDuration = 50;
		int minDuration = 15;
		int duration = (int) (fluidAmount / MultiAmount.INGOT.get()) * baseDuration;
		duration = Math.max(duration, minDuration);
		return duration(duration).coolingDuration((int) (duration * coolingFactor));
	}

	public CastingRecipeBuilder castingDuration(MultiAmount amount) {
		return castingDuration(amount.get());
	}

	public CastingRecipeBuilder keepMold(boolean keep) {
		params.keepMold = keep;
		return self();
	}

	public CastingRecipeBuilder keepMold() {
		return keepMold(true);
	}
}
