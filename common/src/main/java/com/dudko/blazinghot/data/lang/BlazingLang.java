package com.dudko.blazinghot.data.lang;

import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.CATNIP;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.EMI_RECIPE;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.GOGGLES;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.INFO;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.ITEM_GROUP;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.MESSAGE;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.RECIPE;
import static com.dudko.blazinghot.data.lang.BlazingLang.Prefix.RECIPE_TOOLTIP;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.BlazingHot;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.material.Fluid;

public enum BlazingLang {

	DISPLAY_NAME(CATNIP, "display_name", BlazingHot.NAME),

	TAB_BASE(ITEM_GROUP, "Create: Blazing Hot"),
	TAB_BUILDING(ITEM_GROUP, "building", "Blazing Building Blocks"),

	LAMP_LOCKED_MESSAGE(MESSAGE, "modern_lamp.lock", "Lamp locked"),
	LAMP_UNLOCKED_MESSAGE(MESSAGE, "modern_lamp.unlock", "Lamp unlocked"),

	LAMP_GOGGLE_TITLE(GOGGLES, "modern_lamp", "Modern Lamp"),
	LAMP_GOGGLE_STATE(GOGGLES, "modern_lamp.state", "Current state:"),
	LAMP_GOGGLE_LOCKED(GOGGLES, "modern_lamp.locked", "Locked"),
	LAMP_GOGGLE_UNLOCKED(GOGGLES, "modern_lamp.unlocked", "Unlocked"),
	CASTING_GOGGLE_TITLE(GOGGLES, "casting_depot", "Casting Depot Info:"),
	CASTING_GOGGLE_NO_MOLD(GOGGLES, "casting_depot.no_mold", "Empty"),
	CASTING_GOGGLE_COOLING_SPEED(GOGGLES, "casting_depot.cooling_speed", "Cooling speed:"),
	CASTING_GOGGLE_FILLING(GOGGLES, "casting_depot.filling", "Casting"),
	CASTING_GOGGLE_COOLING(GOGGLES, "casting_depot.cooling", "Cooling"),

	NETHER_LAVA_INFO(INFO,
			"nether_lava_cobblestone",
			"You can build faster Cobblestone generators when using Nether Lava instead of regular Lava."),
	BLAZE_MIXER_FUEL(RECIPE_TOOLTIP, "blaze_mixing.fuel", "Blaze Mixer's fuel"),
	MOLD_CONSUMED(RECIPE_TOOLTIP, "spout_casting.consumed", "Consumed on cast"),
	BLAZE_MIXING(RECIPE, "blaze_mixing", "Blaze Mixing"),
	BLAZE_AUTO_SHAPELESS(RECIPE, "blaze_automatic_shapeless", "Blaze Automated Shapeless Crafting"),
	BLAZE_AUTO_BREWING(RECIPE, "blaze_automatic_brewing", "Blaze Automated Brewing"),
	SPOUT_CASTING(RECIPE, "spout_casting", "Casting by Spout"),
	METAL_INTERACTION(RECIPE, "metal_interaction", "Metal Interactions"),

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

	public MutableComponent get(Object... args) {
		return Component.translatable(key, args);
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

	@ExpectPlatform
	public static LangBuilder fluidName(Fluid fluid) {
		throw new AssertionError();
	}

	enum Prefix {
		RECIPE("blazinghot.recipe"),
		RECIPE_TOOLTIP("blazinghot.tooltip"),
		INFO("blazinghot.info"),
		EMI_RECIPE("emi.category.blazinghot"),
		ITEM_GROUP("itemGroup.blazinghot"),
		MESSAGE("message.blazinghot"),
		ITEM("item.blazinghot"),
		GOGGLES("blazinghot.gui.goggles"),
		CATNIP("catnip." + BlazingHot.ID);

		public final String key;

		Prefix(String key) {
			this.key = key;
		}
	}

}
