package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.blazeApple;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.brassApple;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.copperAppple;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.goldenApple;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.ironApple;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.zincApple;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.util.ItemUtil;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class DeployingRecipeGen extends com.simibubi.create.api.data.recipe.DeployingRecipeGen {

	public DeployingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	GeneratedRecipe STELLAR_GOLDEN_APPLE = stellarApple(goldenApple(), BlazingItems.STELLAR_GOLDEN_APPLE),
			STELLAR_BLAZE_APPLE =
					stellarApple(blazeApple(), BlazingItems.STELLAR_BLAZE_APPLE),
			STELLAR_IRON_APPLE =
					stellarApple(ironApple(), BlazingItems.STELLAR_IRON_APPLE),
			STELLAR_ZINC_APPLE =
					stellarApple(zincApple(), BlazingItems.STELLAR_ZINC_APPLE),
			STELLAR_COPPER_APPLE =
					stellarApple(copperAppple(), BlazingItems.STELLAR_COPPER_APPLE),
			STELLAR_BRASS_APPLE =
					stellarApple(brassApple(), BlazingItems.STELLAR_BRASS_APPLE);

	private GeneratedRecipe stellarApple(ItemLike metalApple, ItemLike result) {
		return create(ItemUtil.getItemID(metalApple).getPath(),
				b -> b.require(metalApple).require(Items.NETHER_STAR).output(result));
	}
}
