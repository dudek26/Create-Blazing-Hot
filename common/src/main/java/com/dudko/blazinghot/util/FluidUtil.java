package com.dudko.blazinghot.util;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * Based on {@link net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants}
 */
@SuppressWarnings("UnstableApiUsage")
public class FluidUtil {

    public static final long BUCKET = 81000;
    public static final long BOTTLE = 27000;
    public static final long BLOCK = 81000;
    public static final long INGOT = 9000;
    public static final long NUGGET = 1000;
    public static final long DROPLET = 1;

    public static long fromBucketFraction(long numerator, long denominator) {
        long total = numerator * BUCKET;

        if (total % denominator != 0) {
            throw new IllegalArgumentException("Not a valid number of droplets!");
        }
        else {
            return total / denominator;
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
