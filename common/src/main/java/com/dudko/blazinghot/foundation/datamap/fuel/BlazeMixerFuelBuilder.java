package com.dudko.blazinghot.foundation.datamap.fuel;

import java.util.HashMap;
import java.util.Map;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity.MixingType;

public class BlazeMixerFuelBuilder {

	private float speed;
	private float usage;
	private final Map<MixingType, BlazeMixerFuelDataEntry> overrides;

	BlazeMixerFuelBuilder() {
		this.overrides = new HashMap<>();
		this.speed = 1;
		this.usage = 1;
	}

	public BlazeMixerFuelBuilder speed(float speed) {
		this.speed = speed;
		return this;
	}

	public BlazeMixerFuelBuilder usage(float usage) {
		this.usage = usage;
		return this;
	}

	public BlazeMixerFuelBuilder override(MixingType type, float speed, float usage) {
		overrides.put(type, new BlazeMixerFuelDataEntry(speed, usage));
		return this;
	}

	public BlazeMixerFuelData build() {
		return new BlazeMixerFuelData(new BlazeMixerFuelDataEntry(speed, usage), Map.copyOf(overrides));
	}

}
