package com.dudko.blazinghot.data.conditions;

import java.util.List;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;

public class LoadConditionHelper {

	public static <T> String[] stringValues(List<T> values) {
		if (values.isEmpty()) return new String[]{};
		if (!(values.get(0) instanceof String)) {
			throw new IllegalArgumentException("Condition values must be strings");
		}
		return values.stream().map(c -> (String) c).toArray(String[]::new);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> TagKey<R>[] tagValues(List<T> values) {
		if (values.isEmpty()) return new TagKey[]{};
		if (!(values.get(0) instanceof TagKey<?>)) {
			throw new IllegalArgumentException("Condition values must be tag keys");
		}
		return values.stream().map(c -> (TagKey<R>) c).toArray(TagKey[]::new);
	}

	@ExpectPlatform
	public static String conditionsKey() {
		throw new AssertionError();
	}
}
