package com.dudko.blazinghot.registry;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.util.LangUtil;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class BlazingTags {

	public static void register() {

	}

	public enum Namespace {
		BLAZINGHOT(BlazingHot.ID, true),
		COMMON("c", false);

		public final String id;
		public final boolean alwaysDatagenDefault;

		Namespace(String id, boolean alwaysDatagenDefault) {
			this.id = id;
			this.alwaysDatagenDefault = alwaysDatagenDefault;
		}

		public ResourceLocation asResource(String... path) {
			return ResourceLocation.fromNamespaceAndPath(id, String.join("/", path));
		}
	}

	public enum Blocks {
		MODERN_LAMPS("modern_lamps", true),
		MODERN_LAMP_BLOCKS("modern_lamps/blocks", true),
		MODERN_LAMP_PANELS("modern_lamps/panels", true),
		MODERN_LAMP_QUAD_PANELS("modern_lamps/quad_panels", true),
		MODERN_LAMP_DOUBLE_PANELS("modern_lamps/double_panels", true),
		MODERN_LAMP_HALF_PANELS("modern_lamps/half_panels", true),
		MODERN_LAMP_SMALL_PANELS("modern_lamps/small_panels", true),

		STORAGE_BLOCKS(Namespace.COMMON, "storage_blocks", true),
		BLAZE_GOLD_BLOCKS(Namespace.COMMON, "storage_blocks/blaze_gold", true);

		public final Namespace namespace;
		public final String path;
		public final boolean item;
		public final boolean alwaysDatagen;

		Blocks(String path) {
			this(Namespace.BLAZINGHOT, path);
		}

		Blocks(Namespace namespace, String path) {
			this(namespace, path, false);
		}

		Blocks(String path, boolean item) {
			this(Namespace.BLAZINGHOT, path, item);
		}

		Blocks(Namespace namespace, String path, boolean item) {
			this(namespace, path, item, namespace.alwaysDatagenDefault);
		}

		Blocks(Namespace namespace, String path, boolean item, boolean alwaysDatagen) {
			this.namespace = namespace;
			this.path = path;
			this.item = item;
			this.alwaysDatagen = alwaysDatagen;
		}

		public ResourceLocation resourceLocation() {
			return namespace.asResource(path);
		}

		public TagKey<Block> tag() {
			return blockTag(resourceLocation());
		}

		public TagKey<Item> itemTag() {
			return BlazingTags.itemTag(resourceLocation());
		}
	}

	public enum Items {
		NETHER_FLORA("nether_flora"),
		MOLDS("molds"),
		STURDY_MOLDS("molds/sturdy"),
		CLAY_MOLDS("molds/clay"),
		PORCELAIN_MOLDS("molds/porcelain"),

		FOODS(Namespace.COMMON, "foods"),
		METAL_FOODS("metal_foods"),
		METAL_CARROTS("metal_foods/carrots"),
		METAL_APPLES("metal_foods/apples"),
		STELLAR_METAL_APPLES("metal_foods/stelar_apples"),
		ENCHANTED_METAL_APPLES("metal_foods/enchanted_apples"),

		STORAGE_BLOCKS(Namespace.COMMON, "storage_blocks"),
		INGOTS(Namespace.COMMON, "ingots"),
		NUGGETS(Namespace.COMMON, "nuggets"),
		PLATES(Namespace.COMMON, "plates"),
		RODS(Namespace.COMMON, "rods"),

		BLAZE_GOLD_INGOTS(Namespace.COMMON, "ingots/blaze_gold", false),
		BLAZE_GOLD_NUGGETS(Namespace.COMMON, "nuggets/blaze_gold", false),
		BLAZE_GOLD_PLATES(Namespace.COMMON, "plates/blaze_gold", false),
		BLAZE_GOLD_RODS(Namespace.COMMON, "rods/blaze_gold", false),

		NETHERRACK_DUSTS(Namespace.COMMON, "dusts/netherrack"),
		STONE_DUSTS(Namespace.COMMON, "dusts/stone"),
		SOUL_SAND_DUSTS(Namespace.COMMON, "dusts/soul_sand"),

		WRENCH(Namespace.COMMON, "tools/wrench");

		public final Namespace namespace;
		public final String path;
		public final boolean alwaysDatagen;

		Items(String path) {
			this(Namespace.BLAZINGHOT, path);
		}

		Items(Namespace namespace, String path) {
			this(namespace, path, namespace.alwaysDatagenDefault);
		}

		Items(Namespace namespace, String path, boolean alwaysDatagen) {
			this.namespace = namespace;
			this.path = path;
			this.alwaysDatagen = alwaysDatagen;
		}

		public ResourceLocation resourceLocation() {
			return namespace.asResource(path);
		}

		public TagKey<Item> tag() {
			return itemTag(resourceLocation());
		}
	}

	public enum Fluids {
		BLAZE_MIXER_FUEL(Namespace.BLAZINGHOT, "blaze_mixer_fuel", false),
		NETHER_LAVA(Namespace.COMMON, "nether_lava", true);

		public final Namespace namespace;
		public final String path;
		public final boolean alwaysDatagen;

		Fluids(String path) {
			this(Namespace.BLAZINGHOT, path);
		}

		Fluids(Namespace namespace, String path) {
			this(namespace, path, namespace.alwaysDatagenDefault);
		}

		Fluids(Namespace namespace, String path, boolean alwaysDatagen) {
			this.namespace = namespace;
			this.path = path;
			this.alwaysDatagen = alwaysDatagen;
		}

		public ResourceLocation resourceLocation() {
			return namespace.asResource(path);
		}

		public TagKey<Fluid> tag() {
			return fluidTag(resourceLocation());
		}
	}

	public static <T> TagKey<T> tag(Registry<T> registry, ResourceLocation id) {
		return TagKey.create(registry.key(), id);
	}

	public static TagKey<Block> blockTag(ResourceLocation id) {
		return tag(BuiltInRegistries.BLOCK, id);
	}

	public static TagKey<Block> blockTag(String namespace, String path) {
		return blockTag(ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public static TagKey<Item> itemTag(ResourceLocation id) {
		return tag(BuiltInRegistries.ITEM, id);
	}

	public static TagKey<Item> itemTag(String namespace, String path) {
		return itemTag(ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public static TagKey<Fluid> fluidTag(ResourceLocation id) {
		return tag(BuiltInRegistries.FLUID, id);
	}

	public static TagKey<Fluid> fluidTag(String namespace, String path) {
		return fluidTag(ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public static void provideLangEntries(BiConsumer<String, String> consumer) {
		for (Blocks blockTag : Blocks.values()) {
			if (!blockTag.alwaysDatagen) continue;
			ResourceLocation loc = blockTag.tag().location();
			consumer.accept("tag.block." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
				LangUtil.titleCaseConversion(blockTag.name()).replace('_', ' '));
		}

		for (Items itemTag : Items.values()) {
			if (!itemTag.alwaysDatagen) continue;
			ResourceLocation loc = itemTag.tag().location();
			consumer.accept("tag.item." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
				LangUtil.titleCaseConversion(itemTag.name().replace('_', ' ')));
		}

		for (Fluids fluidTag : Fluids.values()) {
			if (!fluidTag.alwaysDatagen) continue;
			ResourceLocation loc = fluidTag.tag().location();
			String name = LangUtil.titleCaseConversion(fluidTag.name().replace('_', ' '));
			if (fluidTag == Fluids.NETHER_LAVA) name = "Crimson Lava";
			consumer.accept("tag.fluid." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
				name);
		}
	}

}
