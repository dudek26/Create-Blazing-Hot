package com.dudko.blazinghot.multiloader.fabric;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class MultiRegistriesImpl {
	public static Supplier<Item> getItemFromRegistry(ResourceLocation resourceLocation) {
		return () -> BuiltInRegistries.ITEM.get(resourceLocation);
	}

	@NotNull
	public static Supplier<Fluid> getFluidFromRegistry(ResourceLocation resourceLocation) {
		return () -> BuiltInRegistries.FLUID.get(resourceLocation);
	}

	@NotNull
	public static Supplier<Block> getBlockFromRegistry(ResourceLocation resourceLocation) {
		return () -> BuiltInRegistries.BLOCK.get(resourceLocation);
	}
}
