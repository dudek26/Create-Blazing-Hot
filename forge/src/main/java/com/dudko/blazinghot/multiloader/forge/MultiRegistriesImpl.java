package com.dudko.blazinghot.multiloader.forge;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class MultiRegistriesImpl {
	public static Supplier<Item> getItemFromRegistry(ResourceLocation resourceLocation) {
		return () -> ForgeRegistries.ITEMS.getValue(resourceLocation);
	}

	@NotNull
	public static Supplier<Fluid> getFluidFromRegistry(ResourceLocation resourceLocation) {
		return () -> ForgeRegistries.FLUIDS.getValue(resourceLocation);
	}

	@NotNull
	public static Supplier<Block> getBlockFromRegistry(ResourceLocation resourceLocation) {
		return () -> ForgeRegistries.BLOCKS.getValue(resourceLocation);
	}
}
