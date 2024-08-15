package com.dudko.blazinghot.multiloader.fabric;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MultiRegistriesImpl {
    public static Supplier<Item> getItemFromRegistry(ResourceLocation resourceLocation) {
        return () -> BuiltInRegistries.ITEM.get(resourceLocation);
    }

    @NotNull
    public static Supplier<Fluid> getFluidFromRegistry(ResourceLocation resourceLocation) {
        return () -> BuiltInRegistries.FLUID.get(resourceLocation);
    }
}
