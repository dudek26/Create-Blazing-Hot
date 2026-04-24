package com.dudko.blazinghot.compat.dragons_plus.neoforge;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;

import plus.dragons.createdragonsplus.common.registry.CDPFanProcessingTypes;

public class DragonsPlusFanProcessingTypes {

	public static boolean isFreezing(FanProcessingType type) {
		return CDPFanProcessingTypes.FREEZING.get().equals(type);
	}
}
