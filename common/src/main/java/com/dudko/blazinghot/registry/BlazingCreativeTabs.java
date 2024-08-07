package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.multiloader.Env;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlazingCreativeTabs {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static void register() {
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

    @SuppressWarnings("RedundantOperationOnEmptyContainer")
    public static class RegistrateDisplayItemsGenerator implements DisplayItemsGenerator {

        private final boolean addItems;
        private final Tabs tab;

        public RegistrateDisplayItemsGenerator(boolean addItems, Tabs tab) {
            this.addItems = addItems;
            this.tab = tab;
        }

        private static Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = new ReferenceOpenHashSet<>();

            List<ItemProviderEntry<?>> simpleExclusions = List.of(
//                    AllBlocks.REFINED_RADIANCE_CASING - example
            );

            List<ItemEntry<TagDependentIngredientItem>> tagDependentExclusions = List.of(
//                    AllItems.CRUSHED_OSMIUM - example
            );

            for (ItemProviderEntry<?> entry : simpleExclusions) {
                exclusions.add(entry.asItem());
            }

            for (ItemEntry<TagDependentIngredientItem> entry : tagDependentExclusions) {
                TagDependentIngredientItem item = entry.get();
                if (item.shouldHide()) {
                    exclusions.add(entry.asItem());
                }
            }

            return (item) -> exclusions.contains(item) || item instanceof SequencedAssemblyItem;
        }

        private static List<RegistrateDisplayItemsGenerator.ItemOrdering> makeOrderings() {
            List<RegistrateDisplayItemsGenerator.ItemOrdering> orderings = new ReferenceArrayList<>();

            Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleBeforeOrderings = Map.of(
//                    AllItems.EMPTY_BLAZE_BURNER, AllBlocks.BLAZE_BURNER,
//                    AllItems.SCHEDULE, AllBlocks.TRACK_STATION
            );

            Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleAfterOrderings = Map.of(
//                    AllItems.VERTICAL_GEARBOX, AllBlocks.GEARBOX
            );

            simpleBeforeOrderings.forEach((entry, otherEntry) -> orderings.add(ItemOrdering.before(entry.asItem(),
                    otherEntry.asItem())));

            simpleAfterOrderings.forEach((entry, otherEntry) -> orderings.add(ItemOrdering.after(entry.asItem(),
                    otherEntry.asItem())));

            return orderings;
        }

        private static Function<Item, ItemStack> makeStackFunc() {
            Map<Item, Function<Item, ItemStack>> factories = new Reference2ReferenceOpenHashMap<>();

            Map<ItemProviderEntry<?>, Function<Item, ItemStack>> simpleFactories = Map.of(
//                    AllItems.COPPER_BACKTANK, item -> {
//                        ItemStack stack = new ItemStack(item);
//                        stack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants());
//                        return stack;
//                    }
            );

            simpleFactories.forEach((entry, factory) -> factories.put(entry.asItem(), factory));

            return item -> {
                Function<Item, ItemStack> factory = factories.get(item);
                if (factory != null) {
                    return factory.apply(item);
                }
                return new ItemStack(item);
            };
        }

        private static Function<Item, TabVisibility> makeVisibilityFunc() {
            Map<Item, TabVisibility> visibilities = new Reference2ObjectOpenHashMap<>();

            Map<ItemProviderEntry<?>, TabVisibility> simpleVisibilities = Map.of(
//                    AllItems.BLAZE_CAKE_BASE, TabVisibility.SEARCH_TAB_ONLY
            );

            simpleVisibilities.forEach((entry, factory) -> visibilities.put(entry.asItem(), factory));

//            for (BlockEntry<ValveHandleBlock> entry : AllBlocks.DYED_VALVE_HANDLES) {
//                visibilities.put(entry.asItem(), TabVisibility.SEARCH_TAB_ONLY);
//            }

            return item -> {
                TabVisibility visibility = visibilities.get(item);
                return Objects.requireNonNullElse(visibility, TabVisibility.PARENT_AND_SEARCH_TABS);
            };
        }

        @Override
        public void accept(@NotNull ItemDisplayParameters parameters, CreativeModeTab.@NotNull Output output) {
            Predicate<Item> exclusionPredicate = makeExclusionPredicate();
            List<RegistrateDisplayItemsGenerator.ItemOrdering> orderings = makeOrderings();
            Function<Item, ItemStack> stackFunc = makeStackFunc();
            Function<Item, TabVisibility> visibilityFunc = makeVisibilityFunc();

            List<Item> items = new LinkedList<>();
            Predicate<Item>
                    is3d =
                    Env.unsafeRunForDist(() -> () -> item -> Minecraft
                            .getInstance()
                            .getItemRenderer()
                            .getModel(new ItemStack(item), null, null, 0)
                            .isGui3d(), () -> () -> item -> false);
            if (addItems) items.addAll(collectItems(exclusionPredicate, is3d, true));

            items.addAll(collectBlocks(exclusionPredicate));
            if (addItems) items.addAll(collectItems(exclusionPredicate, is3d, false));

            applyOrderings(items, orderings);
            outputAll(output, items, stackFunc, visibilityFunc);
        }

        private List<Item> collectBlocks(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : REGISTRATE.getAll(Registries.BLOCK)) {
                if (!isInCreativeTab(entry, tab.getKey())) continue;
                Item item = entry.get().asItem();
                if (item == Items.AIR) continue;
                if (!exclusionPredicate.test(item)) items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(Predicate<Item> exclusionPredicate, Predicate<Item> is3d, boolean special) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Item> entry : REGISTRATE.getAll(Registries.ITEM)) {
                if (!isInCreativeTab(entry, tab.getKey())) continue;
                Item item = entry.get();
                if (item instanceof BlockItem) continue;
                if (is3d.test(item) != special) continue;
                if (!exclusionPredicate.test(item)) items.add(item);
            }
            return items;
        }

        private static void applyOrderings(List<Item> items, List<ItemOrdering> orderings) {
            for (ItemOrdering ordering : orderings) {
                int anchorIndex = items.indexOf(ordering.anchor());
                if (anchorIndex != -1) {
                    Item item = ordering.item();
                    int itemIndex = items.indexOf(item);
                    if (itemIndex != -1) {
                        items.remove(itemIndex);
                        if (itemIndex < anchorIndex) {
                            anchorIndex--;
                        }
                    }
                    if (ordering.type() == ItemOrdering.Type.AFTER) {
                        items.add(anchorIndex + 1, item);
                    }
                    else {
                        items.add(anchorIndex, item);
                    }
                }
            }
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, TabVisibility> visibilityFunc) {
            for (Item item : items) {
                output.accept(stackFunc.apply(item), visibilityFunc.apply(item));
            }
        }

        private record ItemOrdering(Item item, Item anchor, ItemOrdering.Type type) {
            public static ItemOrdering before(Item item, Item anchor) {
                return new ItemOrdering(item, anchor, ItemOrdering.Type.BEFORE);
            }

            public static ItemOrdering after(Item item, Item anchor) {
                return new ItemOrdering(item, anchor, ItemOrdering.Type.AFTER);
            }

            public enum Type {
                BEFORE,
                AFTER
            }
        }

        @ExpectPlatform
        private static boolean isInCreativeTab(RegistryEntry<?> entry, ResourceKey<CreativeModeTab> tab) {
            throw new AssertionError();
        }

    }
}
