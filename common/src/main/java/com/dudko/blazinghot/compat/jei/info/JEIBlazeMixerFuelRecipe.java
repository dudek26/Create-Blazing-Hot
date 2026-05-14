package com.dudko.blazinghot.compat.jei.info;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelData;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.level.material.Fluid;

public record JEIBlazeMixerFuelRecipe(Fluid fluid, BlazeMixerFuelData data) {
	public static final RecipeType<JEIBlazeMixerFuelRecipe> TYPE =
		RecipeType.create(BlazingHot.ID, "blaze_mixer_fuel", JEIBlazeMixerFuelRecipe.class);
}
