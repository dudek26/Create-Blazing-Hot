package com.dudko.blazinghot.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class WorldUtil {

    public static final HashMap<ResourceKey<Level>, String> DIMENSIONS =
            new HashMap<>(Map.of(Level.OVERWORLD, "overworld", Level.NETHER, "the_nether", Level.END, "the_end"));

    @NotNull
    public static String dimensionToString(ResourceKey<Level> dimension) {
        return DIMENSIONS.getOrDefault(dimension, "unknown");
    }

    @Nullable
    public static ResourceKey<Level> dimensionFromString(String string) {
        for (Map.Entry<ResourceKey<Level>, String> entry : DIMENSIONS.entrySet()) {
            if (entry.getValue().equals(string)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
