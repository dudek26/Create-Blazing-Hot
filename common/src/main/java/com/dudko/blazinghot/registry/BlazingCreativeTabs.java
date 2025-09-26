package com.dudko.blazinghot.registry;

import java.util.function.Supplier;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.data.CreateRegistrate;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class BlazingCreativeTabs {

	protected static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static void register() {
		REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
	}

	public enum Tabs {
		BASE(BlazingCreativeTabs::getBaseTabKey),
		BUILDING(BlazingCreativeTabs::getBuildingTabKey);

		private final Supplier<ResourceKey<CreativeModeTab>> keySupplier;

		Tabs(Supplier<ResourceKey<CreativeModeTab>> keySupplier) {
			this.keySupplier = keySupplier;
		}

		public ResourceKey<CreativeModeTab> getKey() {
			return keySupplier.get();
		}
	}

	@ExpectPlatform
	public static ResourceKey<CreativeModeTab> getBaseTabKey() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static ResourceKey<CreativeModeTab> getBuildingTabKey() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void useBaseTab() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void useBuildingTab() {
		throw new AssertionError();
	}

}
