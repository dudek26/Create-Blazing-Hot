package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
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
