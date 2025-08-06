package com.dudko.blazinghot.config;

import net.createmod.catnip.config.ConfigBase;

public class CClient extends ConfigBase {

	public final ConfigGroup client = group(0, "client", Comments.client);

	public final ConfigBool vanillaAppleTooltips = b(true, "vanillaAppleTooltips", Comments.vanillaAppleTooltips);
	public final ConfigBool foodTooltips = b(true, "modFoodTooltips", Comments.foodEffectTooltips);
	public final ConfigEnum<ModernLampTooltip>
			modernLampGoggleTooltip =
			e(ModernLampTooltip.CROUCH, "modernLampGoggleTooltip", Comments.modernLampGoggleTooltip);
	public final ConfigEnum<CastingProgressIndicator>
			castingProgressIndicator =
			e(CastingProgressIndicator.BAR, "castingProgressIndicator", Comments.castingProgressIndicator);

	@Override
	public String getName() {
		return "client";
	}

	private static class Comments {
		static String
				client =
				"Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!";
		static String foodEffectTooltips = "Show effects of food added by the mod in their tooltips.";
		static String vanillaAppleTooltips = "Show effects of vanilla Golden Apples in their tooltips.";
		static String modernLampGoggleTooltip = "Show goggle tooltips when looking at Modern Lamps.";
		static String
				castingProgressIndicator =
				"Style of goggle casting and cooling progress indicator of a Casting Depot";
	}

	public enum ModernLampTooltip {
		ALWAYS,
		CROUCH,
		NEVER;

		ModernLampTooltip() {

		}

		public boolean shouldRender(boolean isPlayerSneaking) {
			return (this == ALWAYS) || (this == CROUCH && isPlayerSneaking);
		}
	}

	public enum CastingProgressIndicator {
		BAR(false),
		PERCENTAGE(true),
		EXACT(true),
		NONE(true);

		public final boolean inline;

		CastingProgressIndicator(boolean inline) {
			this.inline = inline;
		}
	}
}
