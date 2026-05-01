package com.dudko.blazinghot.compat.jei.category.neoforge;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.compat.jei.category.BlazeMixerFuelCategory;
import com.dudko.blazinghot.compat.jei.info.JEIBlazeMixerFuelRecipe;
import com.dudko.blazinghot.foundation.datamap.BlazeMixerFuelData;
import com.dudko.blazinghot.registry.neoforge.BlazingDataMapsNeoForge;

import net.minecraft.core.registries.BuiltInRegistries;

public class BlazeMixerFuelCategoryImpl extends BlazeMixerFuelCategory {

	protected List<JEIBlazeMixerFuelRecipe> getRecipes() {
		List<JEIBlazeMixerFuelRecipe> recipes = new ArrayList<>();
		BuiltInRegistries.FLUID.holders().forEach(fluid -> {
			BlazeMixerFuelData data = fluid.getData(BlazingDataMapsNeoForge.BLAZE_MIXER_FUEL);
			if (data == null) return;

			recipes.add(new JEIBlazeMixerFuelRecipe(fluid.value(), data.speed(), data.usage()));
		});
		return recipes;
	}

	public static BlazeMixerFuelCategory create() {
		return new BlazeMixerFuelCategoryImpl();
	}
}
