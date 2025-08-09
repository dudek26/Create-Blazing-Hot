package com.dudko.blazinghot.data.conditions;

import java.util.List;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;

public class LoadConditionHelper {

	public static <T> String[] stringValues(List<T> values) {
		if (values.isEmpty()) return new String[]{};
		if (!(values.getFirst() instanceof String)) {
			throw new IllegalArgumentException("Condition values must be strings");
		}
		return values.stream().map(c -> (String) c).toArray(String[]::new);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> TagKey<R>[] tagValues(List<T> values) {
		if (values.isEmpty()) return new TagKey[]{};
		if (!(values.getFirst() instanceof TagKey<?>)) {
			throw new IllegalArgumentException("Condition values must be tag keys");
		}
		return values.stream().map(c -> (TagKey<R>) c).toArray(TagKey[]::new);
	}

	public static <T> ItemLike[] itemLikeValues(List<T> values) {
		if (values.isEmpty()) return new ItemLike[]{};
		if (!(values.getFirst() instanceof ItemLike)) {
			throw new IllegalArgumentException("Condition values must be item likes");
		}
		return values.stream().map(c -> (ItemLike) c).toArray(ItemLike[]::new);
	}

}
