package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.function.BiConsumer;

import static com.dudko.blazinghot.registry.BlazingTags.titleCaseConversion;

@SuppressWarnings("unchecked")
public class CommonTags {

    public enum Blocks {
        STORAGE_BLOCKS("storage_blocks"),
        BLAZE_GOLD_BLOCKS("storage_blocks/blaze_gold", "blaze_gold_blocks");

        public final TagKey<Block> internal;
        public final TagKey<Block> forge;
        public final TagKey<Block> fabric;

        Blocks(String common) {
            this(common, common, common);
        }

        Blocks(String forge, String fabric) {
            this(forge, fabric, fabric);
        }

        Blocks(String forge, String fabric, String internal) {
            this.internal = TagKey.create(BuiltInRegistries.BLOCK.key(), BlazingHot.asResource(internal));
            this.forge = TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation("forge", forge));
            this.fabric = TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation("c", fabric));
        }

        public TagKey<Block>[] bothTags() {
            return (TagKey<Block>[]) new TagKey[]{forge, fabric};
        }

        public TagKey<Block>[] allTags() {
            return (TagKey<Block>[]) new TagKey[]{internal, forge, fabric};
        }

        public static void register() {
        }

    }

    public enum Items {
        STORAGE_BLOCKS("storage_blocks"),

        BLAZE_GOLD_BLOCKS("storage_blocks/blaze_gold", "blaze_gold_blocks"),
        BLAZE_GOLD_INGOTS("ingots/blaze_gold", "blaze_gold_ingots"),
        BLAZE_GOLD_NUGGETS("nuggets/blaze_gold", "blaze_gold_nuggets"),
        BLAZE_GOLD_PLATES("plates/blaze_gold", "blaze_gold_plates"),
        BLAZE_GOLD_RODS("rods/blaze_gold", "blaze_gold_rods"),

        IRON_BLOCKS("storage_blocks/iron", "iron_blocks"),
        IRON_INGOTS("ingots/iron", "iron_ingots"),
        IRON_NUGGETS("nuggets/iron", "iron_nuggets"),
        IRON_PLATES("plates/iron", "iron_plates"),

        GOLD_BLOCKS("storage_blocks/gold", "gold_blocks"),
        GOLD_INGOTS("ingots/gold", "gold_ingots"),
        GOLD_NUGGETS("nuggets/gold", "gold_nuggets"),
        GOLD_PLATES("plates/gold", "gold_plates"),

        COPPER_BLOCKS("storage_blocks/copper", "copper_blocks"),
        COPPER_INGOTS("ingots/copper", "copper_ingots"),
        COPPER_NUGGETS("nuggets/copper", "copper_nuggets"),
        COPPER_PLATES("plates/copper", "copper_plates"),

        ZINC_BLOCKS("storage_blocks/zinc", "zinc_blocks"),
        ZINC_INGOTS("ingots/zinc", "zinc_ingots"),
        ZINC_NUGGETS("nuggets/zinc", "zinc_nuggets"),
        ZINC_PLATES("plates/zinc", "zinc_plates"),

        BRASS_BLOCKS("storage_blocks/brass", "brass_blocks"),
        BRASS_INGOTS("ingots/brass", "brass_ingots"),
        BRASS_NUGGETS("nuggets/brass", "brass_nuggets"),
        BRASS_PLATES("plates/brass", "brass_plates"),

        PLATES("plates"),
        FOODS("foods"),

        NETHERRACK_DUSTS("dusts/netherrack", "netherrack_dusts"),
        STONE_DUSTS("dusts/stone", "stone_dusts"),
        SOUL_SAND_DUSTS("dusts/soul_sand", "soul_sand_dusts");

        public final TagKey<Item> internal;
        public final TagKey<Item> forge;
        public final TagKey<Item> fabric;

        Items(String common) {
            this(common, common, common);
        }

        Items(String forge, String fabric) {
            this(forge, fabric, fabric);
        }

        Items(String forge, String fabric, String internal) {
            this.internal = TagKey.create(BuiltInRegistries.ITEM.key(), BlazingHot.asResource(internal));
            this.forge = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("forge", forge));
            this.fabric = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("c", fabric));
        }

        public TagKey<Item>[] bothTags() {
            return (TagKey<Item>[]) new TagKey[]{forge, fabric};
        }

        public TagKey<Item>[] allTags() {
            return (TagKey<Item>[]) new TagKey[]{internal, forge, fabric};
        }

        public static void register() {
        }

    }

    public enum Fluids {
        MOLTEN_GOLD("molten_gold"),
        MOLTEN_BLAZE_GOLD("molten_blaze_gold"),
        MOLTEN_IRON("molten_iron");

        public final TagKey<Fluid> internal;
        public final TagKey<Fluid> forge;
        public final TagKey<Fluid> fabric;

        Fluids(String common) {
            this(common, common, common);
        }

        Fluids(String forge, String fabric) {
            this(forge, fabric, fabric);
        }

        Fluids(String forge, String fabric, String internal) {
            this.internal = TagKey.create(BuiltInRegistries.FLUID.key(), BlazingHot.asResource(internal));
            this.forge = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation("forge", forge));
            this.fabric = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation("c", fabric));

        }

        public TagKey<Fluid>[] bothTags() {
            return (TagKey<Fluid>[]) new TagKey[]{forge, fabric};
        }

        public TagKey<Fluid>[] allTags() {
            return (TagKey<Fluid>[]) new TagKey[]{internal, forge, fabric};
        }

        public static void register() {
        }

    }

    public static void register() {
        Blocks.register();
        Fluids.register();
        Items.register();
    }

    public static void provideLangEntries(BiConsumer<String, String> consumer) {
        for (Blocks blockTag : Blocks.values()) {
            for (TagKey<Block> tag : blockTag.allTags()) {
                ResourceLocation loc = tag.location();
                consumer.accept("tag.block." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
                                titleCaseConversion(blockTag.name()).replace('_', ' '));
            }
        }
        for (Items itemTag : Items.values()) {
            for (TagKey<Item> tag : itemTag.allTags()) {
                ResourceLocation loc = tag.location();
                consumer.accept("tag.item." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
                                titleCaseConversion(itemTag.name().replace('_', ' ')));
            }
        }
        for (Fluids itemTag : Fluids.values()) {
            for (TagKey<Fluid> tag : itemTag.allTags()) {
                ResourceLocation loc = tag.location();
                consumer.accept("tag.fluid." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
                                titleCaseConversion(itemTag.name().replace('_', ' ')));
            }
        }
    }

}
