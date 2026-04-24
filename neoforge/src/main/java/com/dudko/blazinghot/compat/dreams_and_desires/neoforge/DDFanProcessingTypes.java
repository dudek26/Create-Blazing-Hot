package com.dudko.blazinghot.compat.dreams_and_desires.neoforge;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;

import dev.lopyluna.dndesires.register.DesiresFanProcessingTypes;

public class DDFanProcessingTypes {

	public static boolean isFreezing(FanProcessingType type) {
		return DesiresFanProcessingTypes.FREEZING_TYPE.equals(type);
	}

	public static boolean isSeething(FanProcessingType type) {
		return DesiresFanProcessingTypes.SEETHING_TYPE.equals(type);
	}

}
