package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.AllCreativeModeTabs.TabInfo;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public final class BlazingCreativeTabs {

    public static final TabInfo
            BLAZING_TAB =
            register("blazinghot", FabricItemGroup
                    .builder()
                    .title(Component.translatable("itemGroup.blazinghot"))
                    .icon(() -> BlazingItems.BLAZE_GOLD_INGOT.asItem().getDefaultInstance())
                    .displayItems((params, out) -> {
                        BlazingHot.REGISTRATE
                                .getAll(Registries.ITEM)
                                .stream()
                                .filter(i -> !(i.get() instanceof SequencedAssemblyItem))
                                .map(RegistryEntry::get)
                                .forEach(out::accept);
                    })::build);

    public static ResourceKey<CreativeModeTab> getKey() {
        return BLAZING_TAB.key();
    }

    private static TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = BlazingHot.asResource(name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new TabInfo(key, tab);
    }

    public static CreativeModeTab get() {
        return BLAZING_TAB.tab();
    }

    public static void register() {
    }
}
