package com.dudko.blazinghot.config;

import net.createmod.catnip.config.ConfigBase;

public class CCasting extends ConfigBase {

	public CCasting() {

	}

	// bump this version to reset configured values.
	private static final int VERSION = 1;

	public final ConfigFloat
			splashingCoolingModifier =
			f(0.5f, 0, 100, "splashingCoolingModifier", Comments.splashingCoolingModifier);

	public final ConfigFloat
			blastingCoolingModifier =
			f(-0.25f, -100, 0, "blastingCoolingModifier", Comments.blastingCoolingModifier);

	public final ConfigFloat
			minimumCoolingSpeed =
			f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE, "minimumCoolingSpeed", Comments.minimumCoolingSpeed);

	public final ConfigFloat
			maximumCoolingSpeed =
			f(Float.MAX_VALUE, Float.MIN_VALUE, Float.MAX_VALUE, "maximumCoolingSpeed", Comments.maximumCoolingSpeed);

	@Override
	public String getName() {
		return "casting.v" + VERSION;
	}

	private static class Comments {
		static String splashingCoolingModifier = "Additional cooling factor when splashing a Casting Depot.";
		static String blastingCoolingModifier = "Cooling factor reduction when blasting a Casting Depot.";
		static String minimumCoolingSpeed = "The minimum cooling speed of a Casting Depot.";
		static String maximumCoolingSpeed = "The maximum cooling speed of a Casting Depot.";
	}
}
