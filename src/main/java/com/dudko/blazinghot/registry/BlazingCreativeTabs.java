package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
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
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.dudko.blazinghot.BlazingHot.REGISTRATE;
import static net.minecraft.world.item.DyeColor.WHITE;

public final class BlazingCreativeTabs {

    public static final TabInfo BLAZING_BASE_TAB = register("blazinghot",
            FabricItemGroup
                    .builder()
                    .title(Component.translatable("itemGroup.blazinghot"))
                    .icon(() -> BlazingItems.BLAZE_GOLD_INGOT.asItem().getDefaultInstance())
                    .displayItems((params, out) -> {
                        REGISTRATE.getAll(Registries.ITEM).stream().map(RegistryEntry::get).filter(i -> {
                            List<Item> excludedItems = new ArrayList<>();
                            excludedItems.add(BlazingBlocks.BLAZE_GOLD_BLOCK.get().asItem());
                            excludedItems.addAll(REGISTRATE
                                    .getAll(Registries.BLOCK)
                                    .stream()
                                    .filter(b -> b.get() instanceof ModernLampBlock
                                            || b.get() instanceof ModernLampPanelBlock)
                                    .map(b -> b.get().asItem())
                                    .toList());
                            excludedItems.addAll(REGISTRATE
                                    .getAll(Registries.ITEM)
                                    .stream()
                                    .filter(e -> e.get() instanceof SequencedAssemblyItem)
                                    .map(RegistryEntry::get)
                                    .toList());
                            return !excludedItems.contains(i);
                        }).forEach(out::accept);
                    })::build);
    public static final TabInfo BLAZING_BUILDING_TAB = register("blazinghot_building",
            FabricItemGroup
                    .builder()
                    .title(Component.translatable("itemGroup.blazinghot.building"))
                    .icon(() -> BlazingBlocks.MODERN_LAMP_BLOCKS.get(WHITE).asItem().getDefaultInstance())::build);

    public static ResourceKey<CreativeModeTab> getKey(TabInfo tabInfo) {
        return tabInfo.key();
    }

    private static TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = BlazingHot.asResource(name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new TabInfo(key, tab);
    }

    public static CreativeModeTab get(TabInfo tabInfo) {
        return tabInfo.tab();
    }

    public static void register() {

    }

}
