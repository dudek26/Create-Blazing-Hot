package com.dudko.blazinghot.data.conditions;

import java.util.List;
import java.util.stream.Stream;

import com.dudko.blazinghot.compat.Mods;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;

public class DefaultLoadConditions {

	public static LoadCondition<String> allModsLoaded(String... mods) {
		return LoadCondition.create(LoadCondition.Type.ALL_MODS_LOADED, mods);
	}

	public static LoadCondition<String> allModsLoaded(Mods... mods) {
		return allModsLoaded(Stream.of(mods).map(m -> m.id).toArray(String[]::new));
	}

	public static LoadCondition<String> allModsLoaded(List<Mods> mods) {
		return allModsLoaded(mods.stream().map(m -> m.id).toArray(String[]::new));
	}

	public static LoadCondition<String> anyModLoaded(String... mods) {
		return LoadCondition.create(LoadCondition.Type.ANY_MOD_LOADED, mods);
	}

	public static LoadCondition<String> anyModLoaded(Mods... mods) {
		return anyModLoaded(Stream.of(mods).map(m -> m.id).toArray(String[]::new));
	}

	public static LoadCondition<String> anyModLoaded(List<Mods> mods) {
		return anyModLoaded(mods.stream().map(m -> m.id).toArray(String[]::new));
	}

	public static LoadCondition<LoadCondition<?>> or(LoadCondition<?>... conditions) {
		return LoadCondition.create(LoadCondition.Type.OR, conditions);
	}

	public static LoadCondition<LoadCondition<?>> and(LoadCondition<?>... conditions) {
		return LoadCondition.create(LoadCondition.Type.AND, conditions);
	}

	public static LoadCondition<LoadCondition<?>> not(LoadCondition<?> condition) {
		return LoadCondition.create(LoadCondition.Type.NOT, condition);
	}

	public static LoadCondition<ItemLike> itemsRegistered(ItemLike... items) {
		return LoadCondition.create(LoadCondition.Type.ITEMS_REGISTERED, items);
	}

	@SafeVarargs
	public static <T> LoadCondition<TagKey<T>> tagsPopulated(TagKey<T>... tags) {
		return LoadCondition.create(LoadCondition.Type.TAGS_POPULATED, tags);
	}

}
