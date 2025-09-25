package com.dudko.blazinghot.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ItemUtil {
	public static ResourceLocation getItemID(ItemLike itemLike) {
		return ResourceLocation.parse(itemLike.asItem().toString());
	}
}
