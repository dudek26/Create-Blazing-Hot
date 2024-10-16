package com.dudko.blazinghot.data.lang;

import java.util.HashSet;
import java.util.function.BiConsumer;

public class ItemDescriptions {

	public static final HashSet<BlazingItemDescription> ALL = new HashSet<>();

	public static final BlazingItemDescription START = null,

	EXTINGUISHING_FOOD =
			BlazingItemDescription
					.builder("extinguishing_food")
					.summary("_Extinguishes_ the consumer after being eaten")
					.register(),

	OXYGEN_FOOD =
			BlazingItemDescription
					.builder("oxygen_food")
					.summary("Restores some _oxygen_ allowing you to _survive underwater_ for a bit longer")
					.register(),

	SLOWNESS_REMOVING_FOOD_0 = slownessRemoved(0),

	SLOWNESS_REMOVING_FOOD_1 = slownessRemoved(1),

	SLOWNESS_REMOVING_FOOD_2 = slownessRemoved(2),

	SLOWNESS_REMOVING_FOOD_ANY = slownessRemoved(255),

	BLAZE_ARROW =
			BlazingItemDescription
					.builder("blaze_arrow")
					.summary("Deals _extra damage_ when hitting a target in _The Nether_ dimension")
					.register(),

	MODERN_LAMP =
			BlazingItemDescription
					.builder("modern_lamp")
					.summary(
							"A _modern_ lamp that can be used to _light up_ your builds, either _manually_ or by _redstone_")
					.addBehaviour("When R-Clicked with Empty Hand", "Toggles between the _on_ and _off_ state")
					.addBehaviour("When R-Clicked with Wrench", "_Locks_ the ability of _toggling_ the lamp _by hand_")
					.register(),

	BLAZE_MIXER =
			BlazingItemDescription
					.builder("blaze_mixer")
					.summary(
							"A _Blaze_ variant of the _Mechanical Mixer_ that works _faster_ when _fueled_, and has some _special_ recipes.")
					.register(),

	END = null;

	public static void provideLangEntries(BiConsumer<String, String> consumer) {
		for (BlazingItemDescription item : ALL) {
			item.provideLang(consumer);
		}
	}

	private static BlazingItemDescription slownessRemoved(int maxAmplitude) {
		String summary;
		if (maxAmplitude == 255) summary = "Removes _Slowness_ effect on consumption";
		else
			summary =
					"Removes _Slowness_ effect with _maximum amplitude_ of _" + (maxAmplitude + 1) + "_ on consumption";

		return BlazingItemDescription.builder("slowness_removed_" + maxAmplitude).summary(summary).register();
	}
}
