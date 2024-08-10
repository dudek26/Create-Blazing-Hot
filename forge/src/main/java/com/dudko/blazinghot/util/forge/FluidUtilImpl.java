package com.dudko.blazinghot.util.forge;

public class FluidUtilImpl {

    public static long platformedAmount(long droplets) {
        return droplets / 81;
    }


    public static String platformedName() {
        return "milibuckets";
    }

    public static String conversionNote() {
        return "";
    }
}
