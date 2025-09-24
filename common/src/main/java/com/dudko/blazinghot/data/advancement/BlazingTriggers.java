package com.dudko.blazinghot.data.advancement;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class BlazingTriggers {

	private static final List<BlazingCriterionTriggerBase<?>> triggers = new LinkedList<>();

	public static SimpleBlazingTrigger addSimple(String id) {
		return add(new SimpleBlazingTrigger(id));
	}

	private static <T extends BlazingCriterionTriggerBase<?>> T add(T instance) {
		triggers.add(instance);
		return instance;
	}

	public static void register() {
		triggers.forEach(trigger -> {
			Registry.register(BuiltInRegistries.TRIGGER_TYPES, trigger.getId(), trigger);
		});
	}

}
