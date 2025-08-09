package com.dudko.blazinghot.data.recipe;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipeBuilder;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipeParams;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeGen;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CastingRecipeGen extends BlazingRecipeGen<CastingRecipeParams, CastingRecipe, CastingRecipeBuilder> {

	public CastingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);

		BlazingMetals.ALL.forEach(this::casting);
		Molds.ALL.forEach(this::mold);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return BlazingRecipeTypes.CASTING;
	}

	@Override
	protected CastingRecipeBuilder getBuilder(ResourceLocation id) {
		return getRecipeType().getSerializer();
	}

	private void mold(Molds.Mold mold) {
		Ingredient base;
		if (mold.shape != null) base = Ingredient.of(mold.shape);
		else base = Ingredient.of(Items.IRON_BARS);

		create(BuiltInRegistries.ITEM.getKey(mold.get(Molds.MoldType.STURDY).asItem()),
				b -> b
						.require(base)
						.require(BlazingMetals.STURDY_ALLOY.getFluidTag(), MultiAmount.INGOT.multiply(2))
						.castingDuration(MultiAmount.INGOT.multiply(2))
						.output(mold.get(Molds.MoldType.STURDY)));
	}

	private void casting(BlazingMetal metal) {
		for (BlazingForm form : metal.forms) {
			if (!form.flags.contains(BlazingForm.Flag.CASTING)) continue;
			Molds.Mold mold = form.mold;
			if (mold == null) continue;
			for (Molds.MoldType moldType : Molds.MoldType.values()) {
				if (!moldType.usable) continue;
				for (Mods mod : form.getMods(metal)) {
					String
							name =
							(mod.alwaysIncluded ? "" : "compat/" + mod.id + "/") + form.getCastingRecipeName(moldType,
									metal);
					create(name, b -> {
						b
								.require(mold.get(moldType))
								.require(metal.getFluidTag(), form.amount)
								.duration(form.castingTime)
								.coolingDuration(form.coolingTime)
								.keepMold(moldType.reusable)
								.output(form.getCastingResult(metal, mod));
						if (!mod.alwaysIncluded) {
							b.withConditions(DefaultLoadConditions.anyModLoaded(mod));
						}
						return b;
					});

				}

			}
		}
	}
}
