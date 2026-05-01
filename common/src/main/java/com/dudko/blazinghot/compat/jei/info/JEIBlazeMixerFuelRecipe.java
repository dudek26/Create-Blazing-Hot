package com.dudko.blazinghot.compat.jei.info;

import com.dudko.blazinghot.BlazingHot;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.level.material.Fluid;

public record JEIBlazeMixerFuelRecipe(Fluid fluid, float speed, float usage) {
	public static final RecipeType<JEIBlazeMixerFuelRecipe> TYPE =
		RecipeType.create(BlazingHot.ID, "blaze_mixer_fuel", JEIBlazeMixerFuelRecipe.class);
}
