package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.function.BiConsumer;

import static com.dudko.blazinghot.util.LangUtil.titleCaseConversion;

/**
 * @apiNote Internal tags are used for recipes, advancements etc.
 * <br> Don't use <code>[...]tagOf</code> methods on Forge, as they use <code>BuiltInRegistries</code>!!!
 */
@SuppressWarnings("unchecked")
public class CommonTags {

    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> tagOf(Registry<T> registry, String path, Namespace namespace) {
        return optionalTag(registry, new ResourceLocation(namespace.namespace, path));
    }

    public static TagKey<Block> blockTagOf(String path, Namespace namespace) {
        return tagOf(BuiltInRegistries.BLOCK, path, namespace);
    }

    public static TagKey<Item> itemTagOf(String path, Namespace namespace) {
        return tagOf(BuiltInRegistries.ITEM, path, namespace);
    }

    public static TagKey<Fluid> fluidTagOf(String path, Namespace namespace) {
        return tagOf(BuiltInRegistries.FLUID, path, namespace);
    }


    public enum Namespace {
        INTERNAL(BlazingHot.ID, false),
        FORGE("forge", true),
        COMMON("c", false);

        public final String namespace;
        public final boolean useFolders;

        Namespace(String namespace, boolean useFolders) {
            this.namespace = namespace;
            this.useFolders = useFolders;
        }

        public String tagPath(String folder, String material) {
            boolean useFolders = folder.equals("wires") || this.useFolders;
            return useFolders ? folderTag(folder, material) : plainTag(folder, material);
        }
    }

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
        NETHER_LAVA("nether_lava");

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
        for (Fluids fluidTag : Fluids.values()) {
            for (TagKey<Fluid> tag : fluidTag.allTags()) {
                ResourceLocation loc = tag.location();
                consumer.accept("tag.fluid." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
                        titleCaseConversion(fluidTag.name().replace('_', ' ')));
            }
        }
    }

    public static String folderTag(String folder, String material) {
        return (folder.equalsIgnoreCase("blocks") ? "storage_blocks" : folder) + "/" + material;
    }

    public static String plainTag(String folder, String material) {
        return material + "_" + folder;
    }


}
