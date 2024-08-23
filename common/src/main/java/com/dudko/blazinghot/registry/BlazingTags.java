package com.dudko.blazinghot.registry;

import static com.dudko.blazinghot.registry.BlazingTags.NameSpace.MOD;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.util.LangUtil;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

@SuppressWarnings("unused")
public class BlazingTags {

	public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
		return TagKey.create(registry.key(), id);
	}

	public enum NameSpace {
		MOD(BlazingHot.ID, false, true);

		public final String id;
		public final boolean optionalDefault;
		public final boolean alwaysDatagenDefault;

		NameSpace(String id) {
			this(id, true, false);
		}

		NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
			this.id = id;
			this.optionalDefault = optionalDefault;
			this.alwaysDatagenDefault = alwaysDatagenDefault;
		}

	}

	public enum Blocks {
		MODERN_LAMPS,
		MODERN_LAMP_PANELS;

		public final TagKey<Block> tag;
		public final boolean alwaysDatagen;


		Blocks() {
			this(MOD);
		}

		Blocks(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		Blocks(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		Blocks(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		Blocks(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(BuiltInRegistries.BLOCK, id);
			}
			else {
				tag = TagKey.create(Registries.BLOCK, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Block block) {
			return block.builtInRegistryHolder().is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
		}

		public boolean matches(BlockState state) {
			return state.is(tag);
		}

		public static void register() {
		}
	}

	public enum Fluids {
		BLAZE_MIXER_FUEL,
		NETHER_LAVA;

		public final TagKey<Fluid> tag;
		public final boolean alwaysDatagen;


		Fluids() {
			this(MOD);
		}

		Fluids(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		Fluids(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		Fluids(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		Fluids(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(BuiltInRegistries.FLUID, id);
			}
			else {
				tag = TagKey.create(Registries.FLUID, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Fluid fluid) {
			return fluid.builtInRegistryHolder().is(tag);
		}

		public boolean matches(FluidState state) {
			return state.is(tag);
		}

		public static void register() {
		}
	}

	public static void register() {
		Blocks.register();
		Fluids.register();
		Items.register();
	}

	public enum Items {
		MODERN_LAMPS,
		MODERN_LAMP_PANELS,
		NETHER_FLORA,

		METAL_FOOD,
		METAL_CARROTS,
		METAL_APPLES,
		STELLAR_METAL_APPLES,
		ENCHANTED_METAL_APPLES;


		public final TagKey<Item> tag;
		public final boolean alwaysDatagen;

		Items() {
			this(NameSpace.MOD);
		}

		Items(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		Items(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		Items(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		Items(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(BuiltInRegistries.ITEM, id);
			}
			else {
				tag = TagKey.create(Registries.ITEM, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Item item) {
			return item.builtInRegistryHolder().is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack.is(tag);
		}

		public static void register() {
		}
	}

	public static void provideLangEntries(BiConsumer<String, String> consumer) {
		for (Blocks blockTag : Blocks.values()) {
			ResourceLocation loc = blockTag.tag.location();
			consumer.accept("tag.block." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
					LangUtil.titleCaseConversion(blockTag.name()).replace('_', ' '));
		}

		for (Items itemTag : Items.values()) {
			ResourceLocation loc = itemTag.tag.location();
			consumer.accept("tag.item." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
					LangUtil.titleCaseConversion(itemTag.name().replace('_', ' ')));
		}

		for (Fluids itemTag : Fluids.values()) {
			ResourceLocation loc = itemTag.tag.location();
			consumer.accept("tag.fluid." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'),
					LangUtil.titleCaseConversion(itemTag.name().replace('_', ' ')));
		}
	}

}
