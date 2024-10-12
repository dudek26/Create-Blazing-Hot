package com.dudko.blazinghot.data.lang;

import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.EMI_RECIPE;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.GOGGLES;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.INFO;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.ITEM_GROUP;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.MESSAGE;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.RECIPE;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.RECIPE_TOOLTIP;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum BlazingLang {

	TAB_BASE(ITEM_GROUP, "Create: Blazing Hot"),
	TAB_BUILDING(ITEM_GROUP, "building", "Blazing Building Blocks"),

	LAMP_LOCKED_MESSAGE(MESSAGE, "modern_lamp.lock", "Lamp locked"),
	LAMP_UNLOCKED_MESSAGE(MESSAGE, "modern_lamp.unlock", "Lamp unlocked"),

	LAMP_GOGGLE_TITLE(GOGGLES, "modern_lamp", "Modern Lamp"),
	LAMP_GOGGLE_STATE(GOGGLES, "modern_lamp.state", "Current state:"),
	LAMP_GOGGLE_LOCKED(GOGGLES, "modern_lamp.locked", "Locked"),
	LAMP_GOGGLE_UNLOCKED(GOGGLES, "modern_lamp.unlocked", "Unlocked"),

	NETHER_LAVA_INFO(INFO,
			"nether_lava_cobblestone",
			"You can build faster Cobblestone generators when using Nether Lava instead of regular Lava."),
	BLAZE_MIXER_FUEL(RECIPE_TOOLTIP, "blaze_mixing.fuel", "Blaze Mixer's fuel"),
	BLAZE_MIXING(RECIPE, "blaze_mixing", "Blaze Mixing"),
	BLAZE_AUTO_SHAPELESS(RECIPE, "blaze_automatic_shapeless", "Blaze Automated Shapeless Crafting"),
	BLAZE_AUTO_BREWING(RECIPE, "blaze_automatic_brewing", "Blaze Automated Brewing"),

	EMI_BLAZE_MIXING(EMI_RECIPE, "blaze_mixing", "Blaze Mixing"),
	EMI_BLAZE_AUTO_SHAPELESS(EMI_RECIPE, "blaze_automatic_shapeless", "Blaze Automated Shapeless Crafting"),
	EMI_BLAZE_AUTO_BREWING(EMI_RECIPE, "blaze_automatic_brewing", "Blaze Automated Brewing"),
	;

	public final String key;
	private final String translation;

	BlazingLang(String key, String translation) {
		this.key = key;
		this.translation = translation;
	}

	BlazingLang(Prefix prefix, String key, String translation) {
		this.key = prefix.key + "." + key;
		this.translation = translation;
	}

	BlazingLang(Prefix prefix, String translation) {
		this.key = prefix.key;
		this.translation = translation;
	}

	public MutableComponent get() {
		return Component.translatable(key);
	}

	public LangBuilder translate() {
		String
				key =
				this.key.startsWith(BlazingHot.ID + ".") ? this.key.replaceFirst(BlazingHot.ID + ".", "") : this.key;
		return Lang.builder(BlazingHot.ID).translate(key);
	}

	public static void provideLangEntries(BiConsumer<String, String> consumer) {
		for (BlazingLang lang : values()) {
			consumer.accept(lang.key, lang.translation);
		}
	}

	enum Prefix {
		RECIPE("blazinghot.recipe"),
		RECIPE_TOOLTIP("blazinghot.tooltip"),
		INFO("blazinghot.info"),
		EMI_RECIPE("emi.category.blazinghot"),
		ITEM_GROUP("itemGroup.blazinghot"),
		MESSAGE("message.blazinghot"),
		ITEM("item.blazinghot"),
		GOGGLES("blazinghot.gui.goggles");

		public final String key;

		Prefix(String key) {
			this.key = key;
		}
	}

}
