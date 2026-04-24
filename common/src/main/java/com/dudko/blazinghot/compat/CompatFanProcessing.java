package com.dudko.blazinghot.compat;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class CompatFanProcessing {

	@ExpectPlatform
	public static boolean isFreezing(FanProcessingType type) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isSeething(FanProcessingType type) {
		throw new AssertionError();
	}
}
