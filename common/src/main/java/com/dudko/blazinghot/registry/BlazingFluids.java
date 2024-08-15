package com.dudko.blazinghot.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class BlazingFluids {

    public static void register() {
        platformRegister();
    }

    @ExpectPlatform
    public static void platformRegister() {
        throw new AssertionError();
    }

}
