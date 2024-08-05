package com.dudko.blazinghot.registry.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingCreativeTabs.RegistrateDisplayItemsGenerator;
import com.dudko.blazinghot.registry.BlazingCreativeTabs.Tabs;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllCreativeModeTabs.TabInfo;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class BlazingCreativeTabsImpl {

    private static final TabInfo
            MAIN_TAB =
            register("create_blazing_hot",
                    () -> FabricItemGroup
                            .builder()
                            .icon(() -> BlazingItems.BLAZE_GOLD_INGOT.asItem().getDefaultInstance())
                            .title(Component.translatable("itemGroup.blazinghot"))
                            .displayItems(new RegistrateDisplayItemsGenerator(true, Tabs.BASE))
                            .build()),
            BUILDING_TAB =
                    register("create_blazing_hot_building",
                            () -> FabricItemGroup
                                    .builder()
                                    .icon(() -> BlazingBlocks.BLAZE_GOLD_BLOCK.asItem().getDefaultInstance())
                                    .title(Component.translatable("itemGroup.blazinghot.building"))
                                    .displayItems(new RegistrateDisplayItemsGenerator(false, Tabs.BUILDING))
                                    .build());

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.key();
    }

    public static ResourceKey<CreativeModeTab> getBuildingTabKey() {
        return BUILDING_TAB.key();
    }

    private static TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = BlazingHot.asResource(name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new TabInfo(key, tab);
    }
}
