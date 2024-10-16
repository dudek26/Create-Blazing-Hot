package com.dudko.blazinghot.registry.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingCreativeTabs.RegistrateDisplayItemsGenerator;
import com.dudko.blazinghot.registry.BlazingCreativeTabs.Tabs;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllCreativeModeTabs;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Bus.MOD)
public class BlazingCreativeTabsImpl {

	private static final DeferredRegister<CreativeModeTab>
			REGISTER =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BlazingHot.ID);

	public static final RegistryObject<CreativeModeTab>
			BASE_TAB =
			REGISTER.register("create_blazing_hot",
					() -> CreativeModeTab
							.builder()
							.title(BlazingLang.TAB_BASE.get())
							.withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getKey())
							.icon(() -> BlazingItems.BLAZE_GOLD_INGOT.asItem().getDefaultInstance())
							.displayItems(new RegistrateDisplayItemsGenerator(true, Tabs.BASE))
							.build());

	public static final RegistryObject<CreativeModeTab>
			BUILDING_TAB =
			REGISTER.register("create_blazing_hot_building",
					() -> CreativeModeTab
							.builder()
							.title(BlazingLang.TAB_BUILDING.get())
							.withTabsBefore(BlazingCreativeTabsImpl.BASE_TAB.getKey())
							.icon(() -> BlazingBlocks.MODERN_LAMP_BLOCKS.get(DyeColor.WHITE).asStack())
							.displayItems(new RegistrateDisplayItemsGenerator(false, Tabs.BUILDING))
							.build());

	public static void register(IEventBus eventBus) {
		REGISTER.register(eventBus);
	}

	public static ResourceKey<CreativeModeTab> getBaseTabKey() {
		return BASE_TAB.getKey();
	}

	public static ResourceKey<CreativeModeTab> getBuildingTabKey() {
		return BUILDING_TAB.getKey();
	}

	public static void useBaseTab() {
		BlazingHot.registrate().setCreativeTab(BASE_TAB);
	}

	public static void useBuildingTab() {
		BlazingHot.registrate().setCreativeTab(BUILDING_TAB);
	}

}
