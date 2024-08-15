package com.dudko.blazinghot.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MultiRegistries {

    @ExpectPlatform
    @NotNull
    public static Supplier<Item> getItemFromRegistry(ResourceLocation resourceLocation) {
        throw new AssertionError();
    }

    @ExpectPlatform
    @NotNull
    public static Supplier<Fluid> getFluidFromRegistry(ResourceLocation resourceLocation) {
        throw new AssertionError();
    }
}
