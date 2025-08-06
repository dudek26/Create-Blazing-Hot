package com.dudko.blazinghot.data.conditions.fabric;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.LoadConditionHelper;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;

@SuppressWarnings("unchecked")
public class LoadConditionImpl {

	private static ConditionJsonProvider[] fabricArrayCondition(LoadCondition<LoadCondition<?>> condition) {
		return condition
				.getValues()
				.stream()
				.map(LoadConditionImpl::getFabricCondition)
				.toArray(ConditionJsonProvider[]::new);
	}

	private static <T> ConditionJsonProvider getFabricCondition(LoadCondition<T> condition) {

		switch (condition.getType()) {
			case ANY_MOD_LOADED -> {
				return DefaultResourceConditions.anyModLoaded(LoadConditionHelper.stringValues(condition.getValues()));
			}
			case ALL_MODS_LOADED -> {
				return DefaultResourceConditions.allModsLoaded(LoadConditionHelper.stringValues(condition.getValues()));
			}
			case TAGS_POPULATED -> {
				return DefaultResourceConditions.tagsPopulated(LoadConditionHelper.tagValues(condition.getValues()));
			}
			case AND -> {
				return DefaultResourceConditions.and(fabricArrayCondition((LoadCondition<LoadCondition<?>>) condition));
			}
			case OR -> {
				return DefaultResourceConditions.or(fabricArrayCondition((LoadCondition<LoadCondition<?>>) condition));
			}
			case NOT -> {
				return DefaultResourceConditions.not(getFabricCondition((LoadCondition<T>) condition
						.getValues()
						.get(0)));
			}
			case ITEMS_REGISTERED -> {
				return DefaultResourceConditions.itemsRegistered(LoadConditionHelper.itemLikeValues(condition.getValues()));
			}
			default -> throw new IllegalArgumentException("Unknown fabric condition type: " + condition.getType().id);
		}

	}

	public static <T> JsonObject platformJson(LoadCondition<T> condition) {
		return getFabricCondition(condition).toJson();
	}

}
