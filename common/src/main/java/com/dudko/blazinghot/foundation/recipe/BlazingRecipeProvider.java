package com.dudko.blazinghot.foundation.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.data.recipe.CompactingRecipeGen;
import com.dudko.blazinghot.data.recipe.CrushingRecipeGen;
import com.dudko.blazinghot.data.recipe.CuttingRecipeGen;
import com.dudko.blazinghot.data.recipe.DeployingRecipeGen;
import com.dudko.blazinghot.data.recipe.FillingRecipeGen;
import com.dudko.blazinghot.data.recipe.HauntingRecipeGen;
import com.dudko.blazinghot.data.recipe.ItemApplicationRecipeGen;
import com.dudko.blazinghot.data.recipe.MillingRecipeGen;
import com.dudko.blazinghot.data.recipe.MixingRecipeGen;
import com.dudko.blazinghot.data.recipe.PressingRecipeGen;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class BlazingRecipeProvider extends RecipeProvider {

	public static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();

	public BlazingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {

	}

	public static void registerAllProcessing(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		GENERATORS.add(new CrushingRecipeGen(output, registries));
		GENERATORS.add(new MillingRecipeGen(output, registries));
		GENERATORS.add(new CuttingRecipeGen(output, registries));
		GENERATORS.add(new DeployingRecipeGen(output, registries));
		GENERATORS.add(new MixingRecipeGen(output, registries));
		GENERATORS.add(new CompactingRecipeGen(output, registries));
		GENERATORS.add(new PressingRecipeGen(output, registries));
		GENERATORS.add(new FillingRecipeGen(output, registries));
		GENERATORS.add(new HauntingRecipeGen(output, registries));
		GENERATORS.add(new ItemApplicationRecipeGen(output, registries));
	}


}
