package com.dudko.blazinghot.compat.jei.info;

import java.util.List;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import mezz.jei.api.recipe.RecipeType;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public record JEIMetalInteractionRecipe(BlazingMetal metal, NonNullSupplier<Fluid> fluid,
										List<Pair<NonNullSupplier<Block>, Double>> results) {

	public static final RecipeType<JEIMetalInteractionRecipe> TYPE = RecipeType.create(BlazingHot.ID, "metal_interactions", JEIMetalInteractionRecipe.class);

}
