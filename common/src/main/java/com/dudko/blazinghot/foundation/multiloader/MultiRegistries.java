package com.dudko.blazinghot.foundation.multiloader;

import java.util.function.Supplier;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

@MethodsReturnNonnullByDefault
public class MultiRegistries {

	/**
	 * @deprecated use {@link BuiltInRegistries}
	 */
	@ExpectPlatform
	@Deprecated
	public static Supplier<Item> getItemFromRegistry(ResourceLocation resourceLocation) {
		throw new AssertionError();
	}

	/**
	 * @deprecated use {@link BuiltInRegistries}
	 */
	@ExpectPlatform
	@Deprecated
	public static Supplier<Fluid> getFluidFromRegistry(ResourceLocation resourceLocation) {
		throw new AssertionError();
	}

	/**
	 * @deprecated use {@link BuiltInRegistries}
	 */
	@ExpectPlatform
	@Deprecated
	public static Supplier<Block> getBlockFromRegistry(ResourceLocation resourceLocation) {
		throw new AssertionError();
	}

	/**
	 * @deprecated use {@link BuiltInRegistries}
	 */
	@ExpectPlatform
	@Deprecated
	public static ResourceLocation getFluidId(Fluid fluid) {
		throw new AssertionError();
	}

	/**
	 * @deprecated use {@link BuiltInRegistries}
	 */
	@ExpectPlatform
	@Deprecated
	public static ResourceLocation getItemId(Item item) {
		throw new AssertionError();
	}

	/**
	 * @deprecated use static methods from {@link RegisteredObjectsHelper}
	 */
	@ExpectPlatform
	@Deprecated
	public static RegisteredObjectsHelper getRegisteredObjectsHelper() {
		throw new AssertionError();
	}
}
