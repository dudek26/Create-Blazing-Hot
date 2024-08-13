package com.dudko.blazinghot.multiloader.forge;

import com.dudko.blazinghot.multiloader.MultiFluids.Constants;

public class MultiFluidsConstantsImpl {

    public static long convert(long droplets) {
        for (Constants constant : Constants.values()) {
            if (constant.ignoreInConversion) continue;
            if (constant.droplets == droplets) {
                return constant.mb;
            }
        }
        return droplets / 81;
    }
}
