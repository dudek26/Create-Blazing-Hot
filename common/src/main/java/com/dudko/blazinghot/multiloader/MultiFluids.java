package com.dudko.blazinghot.multiloader;

import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.util.Mth;

import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class MultiFluids {

    public static final float MELTABLE_CONVERSION = 62.5f;

    public enum Constants {
        BUCKET("bucket", 81000),
        BOTTLE("bottle", 27000),
        BLOCK("block", 81000, 1296, 810),
        INGOT("ingot", 9000, 144, 90),
        PLATE("plate", 9000, 144, 90),
        ROD("rod", 4500, 72, 45),
        NUGGET("nugget", 1000, 16, 10),
        MILIBUCKET("milibucket", 81),
        DROPLET("droplet", 1);

        public final String name;
        public final long droplets;
        public final int mb;
        public final int tconstructMb;
        public boolean ignoreInConversion;

        Constants(String name, long droplets, int mb, int tconstructMb) {
            this.name = name;
            this.droplets = droplets;
            this.mb = mb;
            this.tconstructMb = tconstructMb;
            this.ignoreInConversion = false;
        }

        Constants(String name, long droplets, int mb) {
            this(name, droplets, mb, mb);
        }

        Constants(String name, long droplets) {
            this(name, droplets, Mth.ceil(droplets / 81f));
            this.ignoreInConversion = true;
        }

        public long platformed() {
            return convert(droplets);
        }

        public static Constants fromString(String name) {
            for (Constants constant : Constants.values()) {
                if (constant.name.equalsIgnoreCase(name)) {
                    return constant;
                }
            }
            throw new IllegalArgumentException(
                    "Invalid fluid constantAmount name: " + name + ". Must match any of these: " +
                            Arrays.toString(Arrays.stream(Constants.values()).map(c -> c.name).toArray()));
        }

        @ExpectPlatform
        public static long convert(long droplets) {
            throw new AssertionError();
        }
    }

    /**
     * @return Platformed amount
     */
    public static long fromBucketFraction(long numerator, long denominator) {
        long total = numerator * Constants.BUCKET.droplets;

        if (total % denominator != 0) {
            throw new IllegalArgumentException("Not a valid number of droplets!");
        }
        else {
            return platformedAmount(total / denominator);
        }
    }

    @ExpectPlatform
    public static long platformedAmount(long droplets) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String platformedName() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String conversionNote() {
        throw new AssertionError();
    }


}