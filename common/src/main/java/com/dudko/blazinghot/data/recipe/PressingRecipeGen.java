package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.blazeGoldIngot;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

public class PressingRecipeGen extends com.simibubi.create.api.data.recipe.PressingRecipeGen {

	public PressingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	GeneratedRecipe
			BLAZE_GOLD =
			create("blaze_gold_ingot", b -> b.require(blazeGoldIngot()).output(BlazingItems.BLAZE_GOLD_SHEET.get())),
			STURDY_ALLOY =
					create("sturdy_alloy", b -> b.require(BlazingItems.STURDY_ALLOY).output(AllItems.STURDY_SHEET)),
			CLAY_BLANK_MOLD =
					create("clay_blank_mold",
							b -> b.require(Items.CLAY_BALL).output(Molds.BLANK.get(Molds.MoldType.CLAY)));
}
