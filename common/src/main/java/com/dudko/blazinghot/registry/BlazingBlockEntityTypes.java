package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class BlazingBlockEntityTypes {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static void register() {
        platformRegister();
    }

    @ExpectPlatform
    public static void platformRegister() {
        throw new AssertionError();
    }
}
