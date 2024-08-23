package com.dudko.blazinghot.multiloader;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

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
