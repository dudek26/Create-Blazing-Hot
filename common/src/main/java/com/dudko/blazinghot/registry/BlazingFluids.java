package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.material.Fluid;

public class BlazingFluids {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static void register() {
        platformRegister();
    }

    @ExpectPlatform
    public static void platformRegister() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerFluidInteractions() {
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
