package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class BlazingCreativeTabs {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static void register() {
    }

    @ExpectPlatform
    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ResourceKey<CreativeModeTab> getBuildingTabKey() {
        throw new AssertionError();
    }
}
