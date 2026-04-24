package com.dudko.blazinghot.config;

import net.createmod.catnip.config.ConfigBase;

public class CCasting extends ConfigBase {

	public final ConfigFloat
			splashingCoolingModifier =
			f(0.5f, 0, 100, "splashingCoolingModifier", Comments.splashingCoolingModifier);

	public final ConfigFloat
			blastingCoolingModifier =
			f(-0.25f, -100, 0, "blastingCoolingModifier", Comments.blastingCoolingModifier);

	public final ConfigFloat
			freezingCoolingModifier =
			f(1f, 0, 100, "freezingCoolingModifier", Comments.freezingCoolingModifier);

	public final ConfigFloat
			seethingCoolingModifier =
			f(-0.5f, -100, 0, "seethingCoolingModifier", Comments.seetingCoolingModifier);

	public final ConfigFloat
			minimumCoolingSpeed =
			f(-10, -200, 200, "minimumCoolingSpeed", Comments.minimumCoolingSpeed);

	public final ConfigFloat
			maximumCoolingSpeed =
			f(10, -200, 200, "maximumCoolingSpeed", Comments.maximumCoolingSpeed);

	@Override
	public String getName() {
		return "casting";
	}

	private static class Comments {
		static String splashingCoolingModifier = "Additional cooling speed when splashing a Casting Depot.";
		static String blastingCoolingModifier = "Cooling speed reduction when blasting a Casting Depot.";
		static String[] freezingCoolingModifier = {
				"Additional cooling speed when freezing a Casting Depot.",
				"Requires Create: Dragons Plus or Create: Dreams & Desires."
		};
		static String[] seetingCoolingModifier = {
				"Additional cooling speed when freezing a Casting Depot.", "Requires Create: Dreams & Desires."
		};
		static String minimumCoolingSpeed = "The minimum cooling speed of a Casting Depot.";
		static String maximumCoolingSpeed = "The maximum cooling speed of a Casting Depot.";
	}
}
