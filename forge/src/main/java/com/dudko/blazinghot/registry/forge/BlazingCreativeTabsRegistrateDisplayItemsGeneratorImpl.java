package com.dudko.blazinghot.registry.forge;

import com.dudko.blazinghot.registry.BlazingCreativeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public class BlazingCreativeTabsRegistrateDisplayItemsGeneratorImpl {
	public static boolean isInCreativeTab(RegistryEntry<?> entry, ResourceKey<CreativeModeTab> tab) {
		RegistryObject<CreativeModeTab> tabObject;
		if (tab == BlazingCreativeTabs.getBaseTabKey()) {
			tabObject = BlazingCreativeTabsImpl.BASE_TAB;
		}
		else if (tab == BlazingCreativeTabs.getBuildingTabKey()) {
			tabObject = BlazingCreativeTabsImpl.BUILDING_TAB;
		}
		else {
			tabObject = BlazingCreativeTabsImpl.BASE_TAB;
		}
		return CreateRegistrate.isInCreativeTab(entry, tabObject);
	}
}
