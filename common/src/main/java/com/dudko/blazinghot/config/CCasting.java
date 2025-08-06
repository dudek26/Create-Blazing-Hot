package com.dudko.blazinghot.config;

import net.createmod.catnip.config.ConfigBase;

public class CCasting extends ConfigBase {

	public final ConfigFloat
			splashingCoolingModifier =
			f(0.5f, 0, "splashingCoolingModifier", Comments.splashingCoolingModifier);

	public final ConfigFloat
			blastingCoolingModifier =
			f(-0.25f, -100, "blastingCoolingModifier", Comments.blastingCoolingModifier);

	public final ConfigFloat minimumCoolingSpeed = f(-10, -200, "minimumCoolingSpeed", Comments.minimumCoolingSpeed);

	public final ConfigFloat maximumCoolingSpeed = f(10, -200, "maximumCoolingSpeed", Comments.maximumCoolingSpeed);

	@Override
	public String getName() {
		return "casting";
	}

	private static class Comments {
		static String splashingCoolingModifier = "Additional cooling speed when splashing a Casting Depot.";
		static String blastingCoolingModifier = "Cooling speed reduction when blasting a Casting Depot.";
		static String minimumCoolingSpeed = "The minimum cooling speed of a Casting Depot.";
		static String maximumCoolingSpeed = "The maximum cooling speed of a Casting Depot.";
	}
}
