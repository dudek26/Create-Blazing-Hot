package com.dudko.blazinghot.multiloader.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MultiRegistriesImpl {
    public static Supplier<Item> getItemFromRegistry(ResourceLocation resourceLocation) {
        return () -> ForgeRegistries.ITEMS.getValue(resourceLocation);
    }

    @NotNull
    public static Supplier<Fluid> getFluidFromRegistry(ResourceLocation resourceLocation) {
        return () -> ForgeRegistries.FLUIDS.getValue(resourceLocation);
    }
}
