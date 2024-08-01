package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class BlazingFluids {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static final FluidEntry<?>

            MOLTEN_GOLD = createFromLava(REGISTRATE, "molten_gold"),
            MOLTEN_BLAZE_GOLD =
                    createFromLava(REGISTRATE, "molten_blaze_gold"),
            MOLTEN_IRON =
                    createFromLava(REGISTRATE, "molten_iron"),
            NETHER_LAVA =
                    createFromLava(REGISTRATE, "nether_lava", 10, 1);

    public static void register() {
    }

    @ExpectPlatform
    public static FluidEntry<?> createFromLava(CreateRegistrate registrate, String name, int tickRate, int decreaseRate) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FluidEntry<?> createFromLava(CreateRegistrate registrate, String name, int tickRate) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FluidEntry<?> createFromLava(CreateRegistrate registrate, String name) {
        throw new AssertionError();
    }

}
