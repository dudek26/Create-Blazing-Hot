package com.dudko.blazinghot.data.conditions;

import java.util.stream.Stream;

import com.dudko.blazinghot.compat.Mods;

import net.minecraft.tags.TagKey;

public class DefaultLoadConditions {

	public static LoadCondition<String> allModsLoaded(String... mods) {
		return new LoadCondition<>(LoadCondition.Type.ALL_MODS_LOADED, mods);
	}

	public static LoadCondition<String> allModsLoaded(Mods... mods) {
		return allModsLoaded(Stream.of(mods).map(m -> m.id).toArray(String[]::new));
	}

	public static LoadCondition<String> anyModLoaded(String... mods) {
		return new LoadCondition<>(LoadCondition.Type.ANY_MOD_LOADED, mods);
	}

	public static LoadCondition<String> anyModLoaded(Mods... mods) {
		return anyModLoaded(Stream.of(mods).map(m -> m.id).toArray(String[]::new));
	}

	@SafeVarargs
	public static <T> LoadCondition<TagKey<T>> tagsPopulated(TagKey<T>... tags) {
		return new LoadCondition<>(LoadCondition.Type.TAGS_POPULATED, tags);
	}

}
