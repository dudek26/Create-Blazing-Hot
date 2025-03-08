package com.dudko.blazinghot.data.conditions.fabric;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

import java.util.List;

public class LoadConditionHelperImpl {

	public static String conditionsKey() {
		return ResourceConditions.CONDITIONS_KEY;
	}
}
