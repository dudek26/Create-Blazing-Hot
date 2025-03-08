package com.dudko.blazinghot.data.conditions.fabric;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.LoadConditionHelper;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;

public class LoadConditionImpl {

	public static <T> JsonObject platformJson(LoadCondition<T> condition) {

		switch (condition.getType()) {
			case ANY_MOD_LOADED -> {
				return DefaultResourceConditions
						.anyModLoaded(LoadConditionHelper.stringValues(condition.getValues()))
						.toJson();
			}
			case ALL_MODS_LOADED -> {
				return DefaultResourceConditions
						.allModsLoaded(LoadConditionHelper.stringValues(condition.getValues()))
						.toJson();
			}
			case TAGS_POPULATED -> {
				return DefaultResourceConditions
						.tagsPopulated(LoadConditionHelper.tagValues(condition.getValues()))
						.toJson();
			}
			default -> throw new IllegalArgumentException("Unknown condition type: " + condition.getType());
		}

	}

}
