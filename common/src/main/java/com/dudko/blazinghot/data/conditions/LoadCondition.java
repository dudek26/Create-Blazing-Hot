package com.dudko.blazinghot.data.conditions;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class LoadCondition<T> {

	private final Type type;
	private final List<T> values = new ArrayList<>();

	@SafeVarargs
	LoadCondition(Type type, T... values) {
		this.type = type;
		addValues(values);
	}

	public void addValue(T value) {
		values.add(value);
	}

	@SafeVarargs
	public final void addValues(T... values) {
		for (T value : values) {
			addValue(value);
		}
	}

	public List<T> getValues() {
		return values;
	}

	public Type getType() {
		return type;
	}

	public JsonObject toJson() {
		return platformJson(this);
	}

	@ExpectPlatform
	public static <T> JsonObject platformJson(LoadCondition<T> condition) {
		throw new AssertionError();
	}

	public enum Type {

		TAGS_POPULATED("tags_populated"),
		ANY_MOD_LOADED("any_mod_loaded"),
		ALL_MODS_LOADED("all_mods_loaded");

		public final String id;

		Type(String id) {
			this.id = id;
		}

	}
}
