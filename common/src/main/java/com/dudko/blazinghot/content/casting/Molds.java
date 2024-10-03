package com.dudko.blazinghot.content.casting;

import java.util.HashMap;
import java.util.Map;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.item.MoldItem;
import com.dudko.blazinghot.multiloader.BlazingBuilderTransformers;
import com.dudko.blazinghot.multiloader.MultiFluids.Constants;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags.Items;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Molds {

	public static void register() {

	}

	public static final Mold BLANK = new Mold("blank").register();
	public static final Mold INGOT = new Mold("ingot", Constants.INGOT.platformed(), Items.INGOTS.internal).register();
	public static final Mold
			NUGGET =
			new Mold("nugget", Constants.NUGGET.platformed(), Items.NUGGETS.internal).register();
	public static final Mold SHEET = new Mold("sheet", Constants.PLATE.platformed(), Items.PLATES.internal).register();
	public static final Mold ROD = new Mold("rod", Constants.ROD.platformed(), Items.RODS.internal).register();

	public static long getMoldCapacity(ItemStack stack) {
		if (stack == null || stack.isEmpty()) return 2 * Constants.INGOT.platformed();
		else if (stack.getItem() instanceof MoldItem moldItem) {
			return moldItem.capacity;
		}
		else return Constants.INGOT.platformed();
	}

	public static class Mold {

		public final String name;
		public final long capacity;
		public final TagKey<Item> shape;
		public final Map<MoldType, ItemEntry<MoldItem>> items = new HashMap<>();

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
								.item(type.name + "_" + name + "_mold",
										p -> new MoldItem(p, capacity, shape, type.reusable))
								.transform(BlazingBuilderTransformers.mold(name, type))
								.register());
			}

			return this;
		}

		public ItemEntry<MoldItem> get(MoldType type) {
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
