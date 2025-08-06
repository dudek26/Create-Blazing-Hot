package com.dudko.blazinghot.multiloader;

import java.util.function.Supplier;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.platform.services.RegisteredObjectsHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

@MethodsReturnNonnullByDefault
public class MultiRegistries {

	@ExpectPlatform
	public static Supplier<Item> getItemFromRegistry(ResourceLocation resourceLocation) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Supplier<Fluid> getFluidFromRegistry(ResourceLocation resourceLocation) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Supplier<Block> getBlockFromRegistry(ResourceLocation resourceLocation) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static ResourceLocation getFluidId(Fluid fluid) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static ResourceLocation getItemId(Item item) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static RegisteredObjectsHelper<?> getRegisteredObjectsHelper() {
		throw new AssertionError();
	}
}
