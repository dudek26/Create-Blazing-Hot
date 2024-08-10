package com.dudko.blazinghot.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CClient extends ConfigBase {

    public final ConfigGroup client = group(0, "client", Comments.client);

    public final ConfigBool vanillaAppleTooltips = b(true, "vanillaAppleTooltips",
            Comments.vanillaAppleTooltips);
    public final ConfigBool foodTooltips = b(true, "modFoodTooltips",
            Comments.foodEffectTooltips);

    @Override
    public String getName() {
        return "client";
    }

    private static class Comments {
        static String
                client =
                "Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!";
        static String
                foodEffectTooltips =
                "Show effects of food added by the mod in their tooltips.";
        static String
                vanillaAppleTooltips =
                "Show effects of vanilla Golden Apples in their tooltips.";

    }
}
