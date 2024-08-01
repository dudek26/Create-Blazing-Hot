package com.dudko.blazinghot.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings("unchecked")
public class CommonTags {

    public enum Blocks {
        STORAGE_BLOCKS("storage_blocks"),
        BLAZE_GOLD_BLOCKS("storage_blocks/blaze_gold", "blaze_gold_blocks");

        public final TagKey<Block> forge;
        public final TagKey<Block> fabric;

        Blocks(String common) {
            this(common, common);
        }

        Blocks(String forge, String fabric) {
            this.forge = TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation("forge", forge));
            this.fabric = TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation("c", fabric));

        }

        public TagKey<Block>[] bothTags() {
            return (TagKey<Block>[]) new TagKey[]{forge, fabric};
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
        PLATES("plates"),
        DUSTS("dusts"),
        NETHERRACK_DUSTS("dusts/netherrack", "netherrack_dusts"),
        STONE_DUSTS("dusts/stone", "stone_dusts"),
        SOUL_SAND_DUSTS("dusts/soul_sand", "soul_sand_dusts");

        public final TagKey<Item> forge;
        public final TagKey<Item> fabric;

        Items(String common) {
            this(common, common);
        }

        Items(String forge, String fabric) {
            this.forge = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("forge", forge));
            this.fabric = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("c", fabric));
        }

        public TagKey<Item>[] bothTags() {
            return (TagKey<Item>[]) new TagKey[]{forge, fabric};
        }

        public static void register() {
        }

    }

    public enum Fluids {
        MOLTEN_GOLD("molten_gold"),
        MOLTEN_BLAZE_GOLD("molten_blaze_gold"),
        MOLTEN_IRON("molten_iron");

        public final TagKey<Fluid> forge;
        public final TagKey<Fluid> fabric;

        Fluids(String common) {
            this(common, common);
        }

        Fluids(String forge, String fabric) {
            this.forge = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation("forge", forge));
            this.fabric = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation("c", fabric));

        }

        public TagKey<Fluid>[] bothTags() {
            return (TagKey<Fluid>[]) new TagKey[]{forge, fabric};
        }

        public static void register() {
        }

    }

    public static void register() {
        Blocks.register();
        Fluids.register();
        Items.register();
    }

}
