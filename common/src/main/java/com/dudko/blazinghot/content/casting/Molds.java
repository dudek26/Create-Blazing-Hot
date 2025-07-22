package com.dudko.blazinghot.content.casting;

import java.util.HashMap;
import java.util.Map;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.multiloader.BlazingBuilderTransformers;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags.Items;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Molds {

	public static void register() {

	}

	public static final Mold BLANK = new Mold("blank").register();
	public static final Mold INGOT = new Mold("ingot", MultiAmount.INGOT.get(), Items.INGOTS.tag()).register();
	public static final Mold NUGGET = new Mold("nugget", MultiAmount.NUGGET.get(), Items.NUGGETS.tag()).register();
	public static final Mold SHEET = new Mold("sheet", MultiAmount.INGOT.get(), Items.PLATES.tag()).register();
	public static final Mold ROD = new Mold("rod", MultiAmount.ROD.get(), Items.RODS.tag()).register();

	public static class Mold {

		public final String name;
		public final long capacity;
		public final TagKey<Item> shape;
		public final Map<MoldType, ItemEntry<? extends Item>> items = new HashMap<>();

		public Mold(String name) {
			this(name, 0, null);
		}

		public Mold(String name, long capacity, TagKey<Item> shape) {
			this.name = name;
			this.capacity = capacity;
			this.shape = shape;
		}

		private Mold register() {
			return register(BlazingHot.registrate());
		}

		public Mold register(AbstractRegistrate<?> registrate) {
			for (MoldType type : MoldType.values()) {
				items.put(type,
						registrate
								.item(type.name + "_" + name + "_mold", Item::new)
								.transform(BlazingBuilderTransformers.mold(name, type))
								.register());
			}

			return this;
		}

		public ItemEntry<? extends Item> get(MoldType type) {
			return items.get(type);
		}

	}

	public enum MoldType {
		STURDY("sturdy", BlazingTags.Items.STURDY_MOLDS.tag, true, true);

		public final String name;
		public final TagKey<Item> tag;
		public final boolean reusable;
		public final boolean fireResistant;

		MoldType(String name, TagKey<Item> tag, boolean reusable, boolean fireResistant) {
			this.name = name;
			this.tag = tag;
			this.reusable = reusable;
			this.fireResistant = fireResistant;
		}
	}
}
