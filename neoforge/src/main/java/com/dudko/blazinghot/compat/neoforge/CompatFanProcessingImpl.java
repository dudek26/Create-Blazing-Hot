package com.dudko.blazinghot.compat.neoforge;

import com.dudko.blazinghot.compat.CompatFanProcessing;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.compat.dragons_plus.neoforge.DragonsPlusFanProcessingTypes;
import com.dudko.blazinghot.compat.dreams_and_desires.neoforge.DDFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;

/**
 * @see CompatFanProcessing
 */
public class CompatFanProcessingImpl {

	public static boolean isFreezing(FanProcessingType type) {
		return Mods.CREATE_DRAGONS_PLUS.getIfLoaded(() -> DragonsPlusFanProcessingTypes.isFreezing(type), false)
				|| Mods.CREATE_DREAMS_AND_DESIRES.getIfLoaded(() -> DDFanProcessingTypes.isFreezing(type), false);
	}

	public static boolean isSeething(FanProcessingType type) {
		return Mods.CREATE_DREAMS_AND_DESIRES.getIfLoaded(() -> DDFanProcessingTypes.isSeething(type), false);
	}

}
