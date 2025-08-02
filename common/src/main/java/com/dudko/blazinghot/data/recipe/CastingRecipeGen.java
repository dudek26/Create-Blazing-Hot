package com.dudko.blazinghot.data.recipe;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds.Mold;
import com.dudko.blazinghot.content.casting.Molds.MoldType;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;

@SuppressWarnings({"unused"})
public class CastingRecipeGen extends BlazingProcessingRecipeGen {

	public CastingRecipeGen(PackOutput output) {
		super(output);
		BlazingMetals.ALL.forEach(this::casting);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return BlazingRecipeTypes.CASTING.get();
	}

	private void casting(BlazingMetal metal) {
		for (BlazingForm form : metal.forms) {
			if (!form.flags.contains(BlazingForm.Flag.CASTING)) continue;
			Mold mold = form.mold;
			if (mold == null) continue;
			for (MoldType moldType : MoldType.values()) {
				List<Mods> usedMods = new ArrayList<>();
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
								.toolNotConsumed(moldType.reusable)
								.output(form.getCastingResult(metal, mod));
						if (!mod.alwaysIncluded) {
							b.withCondition(DefaultLoadConditions.anyModLoaded(mod));
							if (!usedMods.isEmpty()) {
								b.withCondition(DefaultLoadConditions.not(DefaultLoadConditions.anyModLoaded(usedMods)));
							}
						}

						usedMods.add(mod);
						return b;
					});

				}

			}
		}
	}

}

