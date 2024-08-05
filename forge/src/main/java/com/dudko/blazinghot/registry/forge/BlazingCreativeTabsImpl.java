package com.dudko.blazinghot.registry.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingCreativeTabs.RegistrateDisplayItemsGenerator;
import com.dudko.blazinghot.registry.BlazingCreativeTabs.Tabs;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.EventBus;
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
                            .title(Components.translatable("itemGroup.create.base"))
                            .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getKey())
                            .icon(() -> BlazingItems.BLAZE_GOLD_INGOT.asItem().getDefaultInstance())
                            .displayItems(new RegistrateDisplayItemsGenerator(true, Tabs.BASE))
                            .build());

    public static final RegistryObject<CreativeModeTab>
            BUILDING_TAB =
            REGISTER.register("create_blazing_hot_building",
                    () -> CreativeModeTab
                            .builder()
                            .title(Components.translatable("itemGroup.create.palettes"))
                            .withTabsBefore(BlazingCreativeTabsImpl.BASE_TAB.getKey())
                            .icon(BlazingBlocks.BLAZE_GOLD_BLOCK::asStack)
                            .displayItems(new RegistrateDisplayItemsGenerator(false, Tabs.BUILDING))
                            .build());

    public static void register(EventBus eventBus) {
        REGISTER.register(eventBus);
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return BASE_TAB.getKey();
    }

    public static ResourceKey<CreativeModeTab> getBuildingTabKey() {
        return BUILDING_TAB.getKey();
    }

}
