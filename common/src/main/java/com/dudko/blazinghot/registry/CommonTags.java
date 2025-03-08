package com.dudko.blazinghot.registry;

import static com.dudko.blazinghot.util.LangUtil.titleCaseConversion;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.BlazingHot;

import dev.architectury.platform.Platform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/**
 * @apiNote Internal tags are used for recipes, advancements etc.
 * <br> Don't use <code>[...]tagOf</code> methods on Forge, as they use <code>BuiltInRegistries</code>!!!
 */
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

	public static TagKey<Block> blockTagOf(String folder, String path, Namespace namespace) {
		return blockTagOf(namespace.tagPath(folder, path), namespace);
	}

	public static TagKey<Item> itemTagOf(String path, Namespace namespace) {
		return tagOf(BuiltInRegistries.ITEM, path, namespace);
	}

	public static TagKey<Item> itemTagOf(String folder, String path, Namespace namespace) {
		return itemTagOf(namespace.tagPath(folder, path), namespace);
	}

	public static TagKey<Fluid> fluidTagOf(String path, Namespace namespace) {
		return tagOf(BuiltInRegistries.FLUID, path, namespace);
	}

	public static TagKey<Fluid> fluidTagOf(String folder, String path, Namespace namespace) {
		return fluidTagOf(namespace.tagPath(folder, path), namespace);
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

		public static Namespace platform() {
			if (Platform.isFabric()) return COMMON;
			if (Platform.isForge()) return FORGE;
			return INTERNAL;
		}

		public ResourceLocation asResource(String path) {
			return new ResourceLocation(namespace, path);
		}
	}

	public enum Blocks {
		STORAGE_BLOCKS("storage_blocks"),
		BLAZE_GOLD_BLOCKS("storage_blocks/blaze_gold", "blaze_gold_blocks");

		private final TagKey<Block> internal;
		private final TagKey<Block> forge;
		private final TagKey<Block> fabric;

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

		public TagKey<Block> tag() {
			if (Platform.isForge()) return forge;
			if (Platform.isFabric()) return fabric;
			return internal;
		}

		public static void register() {
		}

	}

	public enum Items {
		STORAGE_BLOCKS("storage_blocks"),

		BLAZE_GOLD_BLOCKS("storage_blocks/blaze_gold", "blaze_gold_blocks"),
		BLAZE_GOLD_INGOTS("ingots/blaze_gold", "blaze_gold_ingots", false),
		BLAZE_GOLD_NUGGETS("nuggets/blaze_gold", "blaze_gold_nuggets", false),
		BLAZE_GOLD_PLATES("plates/blaze_gold", "blaze_gold_plates", false),
		BLAZE_GOLD_RODS("rods/blaze_gold", "blaze_gold_rods", false),

		PLATES("plates"),
		FOODS("foods"),

		NETHERRACK_DUSTS("dusts/netherrack", "netherrack_dusts"),
		STONE_DUSTS("dusts/stone", "stone_dusts"),
		SOUL_SAND_DUSTS("dusts/soul_sand", "soul_sand_dusts");

		private final TagKey<Item> internal;
		private final TagKey<Item> forge;
		private final TagKey<Item> fabric;
		public final boolean alwaysDatagen;

		Items(String common) {
			this(common, common, common, true);
		}

		Items(String forge, String fabric) {
			this(forge, fabric, fabric, true);
		}

		Items(String common, boolean alwaysDatagen) {
			this(common, common, common, alwaysDatagen);
		}

		Items(String forge, String fabric, boolean alwaysDatagen) {
			this(forge, fabric, fabric, alwaysDatagen);
		}

		Items(String forge, String fabric, String internal, boolean alwaysDatagen) {
			this.internal = TagKey.create(BuiltInRegistries.ITEM.key(), BlazingHot.asResource(internal));
			this.forge = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("forge", forge));
			this.fabric = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("c", fabric));
			this.alwaysDatagen = alwaysDatagen;
		}

		public TagKey<Item> tag() {
			if (Platform.isForge()) return forge;
			if (Platform.isFabric()) return fabric;
			return internal;
		}

		public static void register() {
		}

	}

	public enum Fluids {
		;

		private final TagKey<Fluid> internal;
		private final TagKey<Fluid> forge;
		private final TagKey<Fluid> fabric;

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

		public TagKey<Fluid> tag() {
			if (Platform.isForge()) return forge;
			if (Platform.isFabric()) return fabric;
			return internal;
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
			ResourceLocation loc = blockTag.tag().location();
			consumer.accept("tag.block." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
					titleCaseConversion(blockTag.name()).replace('_', ' '));

		}
		for (Items itemTag : Items.values()) {
			if (!itemTag.alwaysDatagen) continue;
			ResourceLocation loc = itemTag.tag().location();
			consumer.accept("tag.item." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
					titleCaseConversion(itemTag.name().replace('_', ' ')));

		}
		for (Fluids fluidTag : Fluids.values()) {
			ResourceLocation loc = fluidTag.tag().location();
			consumer.accept("tag.fluid." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
					titleCaseConversion(fluidTag.name().replace('_', ' ')));

		}
	}

	public static String folderTag(String folder, String material) {
		return (folder.equalsIgnoreCase("blocks") ? "storage_blocks" : folder) + "/" + material;
	}

	public static String plainTag(String folder, String material) {
		return material + "_" + folder;
	}


}
