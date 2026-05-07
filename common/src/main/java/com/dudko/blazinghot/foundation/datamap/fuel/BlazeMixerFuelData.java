package com.dudko.blazinghot.foundation.datamap.fuel;

import java.util.HashMap;
import java.util.Map;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;

public record BlazeMixerFuelData(Object2ObjectMap<MixingType, BlazeMixerFuelDataEntry> entries) {

	private static final Map<Holder<Fluid>, BlazeMixerFuelDataEntry> FUELS = new HashMap<>();
	public static boolean updated = false;

	public static Map<Holder<Fluid>, BlazeMixerFuelDataEntry> getFuels() {
		if (!updated) {
			updateFuels();
		}
		return FUELS;
	}

	private static void updateFuels() {
		FUELS.clear();
		BuiltInRegistries.FLUID.holders().forEach(fluid -> {
			BlazeMixerFuelDataEntry data = getData(fluid);
			if (data == null) return;
			FUELS.put(fluid, data);
		});
		updated = true;
	}

	@ExpectPlatform
	public static BlazeMixerFuelDataEntry getData(Holder<Fluid> fluid) {
		throw new AssertionError();
	}

	public static BlazeMixerFuelDataEntry getFuelData(Holder<Fluid> fluid) {
		return FUELS.get(fluid);
	}

	public static BlazeMixerFuelDataEntry getFuelData(Fluid fluid) {
		return getFuels().get(BuiltInRegistries.FLUID.wrapAsHolder(fluid));
	}

	public enum MixingType {
		MIXING,
		AUTO_SHAPELESS,
		AUTO_BREWING,
		BLAZE_MIXING
	}
}
