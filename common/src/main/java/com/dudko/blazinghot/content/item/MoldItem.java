package com.dudko.blazinghot.content.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MoldItem extends Item {

	public final long capacity;
	public final TagKey<Item> shape;
	public final boolean reusable;

	public MoldItem(Properties properties, long capacity, TagKey<Item> shape, boolean reusable) {
		super(properties);
		this.capacity = capacity;
		this.shape = shape;
		this.reusable = reusable;
	}
}
