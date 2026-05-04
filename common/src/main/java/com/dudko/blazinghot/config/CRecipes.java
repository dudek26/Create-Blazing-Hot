package com.dudko.blazinghot.config;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluids;

import net.createmod.catnip.config.ConfigBase;

public class CRecipes extends ConfigBase {

	private static final int DEFAULT_FUEL_USAGE = (int) MultiAmount.fromBucketFraction(1, 40).get();

	public final ConfigBool
		allowMixingInBlazeMixer =
		b(true, "allowMixingInBlazeMixer", Comments.allowMixingInBlazeMixer);
	public final ConfigInt
		fueledMixingFuelUsage =
		i(DEFAULT_FUEL_USAGE, 0, Integer.MAX_VALUE, "fueledMixingFuelUsage", Comments.durationToFuelConvertion);
	public final ConfigFloat
		fueledMixingSpeedMultiplier =
		f(1, 1, "fueledMixingSpeedMultiplier", Comments.fueledMixingSpeedMultiplier);
	public final ConfigBool
		allowBrewingInBlazeMixer =
		b(true, "allowBrewingInBlazeMixer", Comments.allowBrewingInBlazeMixer);
	public final ConfigInt
		fueledBrewingFuelUsage =
		i(DEFAULT_FUEL_USAGE, 0, Integer.MAX_VALUE, "fueledBrewingFuelUsage", Comments.fueledBrewingFuelUsage);
	public final ConfigFloat
		fueledBrewingSpeedMultiplier =
		f(1, 1, "fueledBrewingSpeedMultiplier", Comments.fueledBrewingSpeedMultiplier);
	public final ConfigBool
		allowShapelessInBlazeMixer =
		b(true, "allowShapelessInBlazeMixer", Comments.allowShapelessInBlazeMixer);
	public final ConfigInt
		fueledShapelessFuelUsage =
		i(DEFAULT_FUEL_USAGE, 0, Integer.MAX_VALUE, "fueledShapelessFuelUsage", Comments.fueledShapelessFuelUsage);
	public final ConfigFloat
		fueledShapelessSpeedMultiplier =
		f(1, 1, "fueledShapelessSpeedMultiplier", Comments.fueledShapelessSpeedMultiplier);

	private static class Comments {
		static String[] durationToFuelConvertion = new String[]{
			"[in " + MultiFluids.platformedName() + "]",
			MultiFluids.conversionNote(),
			"The amount of fuel that Blaze Mixer uses per 100 processing ticks (default speed) of regular mixing recipes.",
		};
		static String[] fueledBrewingFuelUsage = new String[]{
			"[in " + MultiFluids.platformedName() + "]",
			MultiFluids.conversionNote(),
			"The amount of fuel that Blaze Mixer uses for each brewing recipe.",
		};
		static String[] fueledShapelessFuelUsage = new String[]{
			"[in " + MultiFluids.platformedName() + "]",
			MultiFluids.conversionNote(),
			"The amount of fuel that Blaze Mixer uses for each shapeless crafting recipe.",
		};
		static String
			fueledMixingSpeedMultiplier =
			"Fueled Blaze Mixer's extra speed multiplier when processing mixing recipes";
		static String
			fueledShapelessSpeedMultiplier =
			"Fueled Blaze Mixer's extra speed multiplier when processing shapeless crafting recipes";
		static String
			fueledBrewingSpeedMultiplier =
			"Fueled Blaze Mixer's extra speed multiplier when processing brewing recipes";
		static String recipeReloadNoteLong = "Make sure to run /reload command after changing this option";

		static String[] allowMixingInBlazeMixer = {"Allow regular mixing recipes in the Blaze Mixer", recipeReloadNoteLong};
		static String[] allowBrewingInBlazeMixer = {"Allow brewing recipes in the Blaze Mixer", recipeReloadNoteLong};
		static String[] allowShapelessInBlazeMixer = {"Allow shapeless crafting recipes in the Blaze Mixer", recipeReloadNoteLong};

	}

	@Override
	public String getName() {
		return "recipes";
	}
}
