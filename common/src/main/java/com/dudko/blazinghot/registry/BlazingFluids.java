package com.dudko.blazinghot.registry;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.material.Fluid;

public class BlazingFluids {

    public static void register() {
        platformRegister();
    }

    @ExpectPlatform
    public static void platformRegister() {
        throw new AssertionError();
    }

    public enum MultiloaderFluids {
        MOLTEN_GOLD,
        MOLTEN_BLAZE_GOLD,
        MOLTEN_IRON,
        NETHER_LAVA;

        public FluidEntry<?> entry() {
            return getEntry(this);
        }

        public Fluid get() {
            return entry().get();
        }
    }

    @ExpectPlatform
    public static FluidEntry<?> getEntry(MultiloaderFluids fluid) {
        throw new AssertionError();
    }

}
