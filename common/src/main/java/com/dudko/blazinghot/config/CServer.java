package com.dudko.blazinghot.config;

import com.dudko.blazinghot.util.FluidUtil;
import com.simibubi.create.foundation.config.ConfigBase;

public class CServer extends ConfigBase {

    private static final int DEFAULT_FUEL_USAGE = (int) FluidUtil.platformedAmount(FluidUtil.fromBucketFraction(1, 40));

    public final ConfigBase.ConfigGroup recipes = group(0, "recipes", Comments.recipes);
    public final ConfigBool
            allowMixingInBlazeMixer =
            b(true, "allowMixingInBlazeMixer", Comments.allowMixingInBlazeMixer);
    public final ConfigInt
            blazeMixingFuelUsage =
            i(DEFAULT_FUEL_USAGE, 0, Integer.MAX_VALUE, "blazeMixingFuelUsage", Comments.durationToFuelConvertion);
    public final ConfigFloat
            blazeMixingSpeedMultiplier =
            f(2, 1, "blazeMixingSpeedMultiplier", Comments.blazeMixingSpeedMultiplier);
    public final ConfigBool
            allowBrewingInBlazeMixer =
            b(true, "allowBrewingInBlazeMixer", Comments.allowBrewingInBlazeMixer);
    public final ConfigInt
            blazeBrewingFuelUsage =
            i(DEFAULT_FUEL_USAGE, 0, Integer.MAX_VALUE, "blazeBrewingFuelUsage", Comments.blazeBrewingFuelUsage);
    public final ConfigFloat
            blazeBrewingSpeedMultiplier =
            f(2, 1, "blazeBrewingSpeedMultiplier", Comments.blazeBrewingSpeedMultiplier);
    public final ConfigBool
            allowShapelessInBlazeMixer =
            b(true, "allowShapelessInBlazeMixer", Comments.allowShapelessInBlazeMixer);
    public final ConfigInt
            blazeShapelessFuelUsage =
            i(DEFAULT_FUEL_USAGE, 0, Integer.MAX_VALUE, "blazeShapelessFuelUsage", Comments.blazeShapelessFuelUsage);
    public final ConfigFloat
            blazeShapelessSpeedMultiplier =
            f(2, 1, "blazeShapelessSpeedMultiplier", Comments.blazeShapelessSpeedMultiplier);


    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String recipes = "Create: Blazing Hot's Recipes' settings";
        static String[] durationToFuelConvertion = new String[]{
                "[in " + FluidUtil.platformedName() + "]",
                FluidUtil.conversionNote(),
                "The amount of fuel that Blaze Mixer uses per 100 processing ticks (default speed) of regular mixing recipes.",
                "Does not affect melting recipes.",
        };
        static String[] blazeBrewingFuelUsage = new String[]{
                "[in " + FluidUtil.platformedName() + "]",
                FluidUtil.conversionNote(),
                "The amount of fuel that Blaze Mixer uses for each brewing recipe.",
        };
        static String[] blazeShapelessFuelUsage = new String[]{
                "[in " + FluidUtil.platformedName() + "]",
                FluidUtil.conversionNote(),
                "The amount of fuel that Blaze Mixer uses for each shapeless crafting recipe.",
        };
        static String
                blazeMixingSpeedMultiplier =
                "Fueled Blaze Mixer's speed multiplier when processing mixing recipes";
        static String
                blazeShapelessSpeedMultiplier =
                "Fueled Blaze Mixer's speed multiplier when processing shapeless crafting recipes";
        static String
                blazeBrewingSpeedMultiplier =
                "Fueled Blaze Mixer's speed multiplier when processing brewing recipes";
        static String allowMixingInBlazeMixer = "Allow regular mixing recipes in the Blaze Mixer";
        static String allowBrewingInBlazeMixer = "Allow brewing recipes in the Blaze Mixer";
        static String allowShapelessInBlazeMixer = "Allow shapeless crafting recipes in the Blaze Mixer";
    }
}
