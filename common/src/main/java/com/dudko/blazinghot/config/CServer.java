package com.dudko.blazinghot.config;

import com.dudko.blazinghot.util.FluidUtil;
import com.simibubi.create.foundation.config.ConfigBase;

public class CServer extends ConfigBase {

    public final ConfigBase.ConfigGroup recipes = group(0, "recipes", Comments.recipes);
    public final ConfigInt
            durationToFuelConversion =
            i((int) FluidUtil.platformedAmount(FluidUtil.fromBucketFraction(1, 40)),
                    0,
                    Integer.MAX_VALUE,
                    "durationToFuelConvertion",
                    Comments.durationToFuelConvertion);


    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String recipes = "Create: Blazing Hot's Recipes' settings";
        static String[] durationToFuelConvertion = new String[]{
                "[in " + FluidUtil.platformedName() + "]",
                "The amount of fuel that Blaze Mixer uses per 100 processing ticks of regular mixing recipes.",
                FluidUtil.conversionNote()
        };
    }
}
