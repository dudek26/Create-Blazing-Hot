package com.dudko.blazinghot.data.lang;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.gui.ponder.BlazingPonderPlugin;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingTags;
import com.tterrag.registrate.providers.RegistrateLangProvider;

public class BlazingLangGen {

	public static void generate(RegistrateLangProvider provider) {
		BiConsumer<String, String> langConsumer = provider::add;

		BlazingTags.provideLangEntries(langConsumer);
		BlazingMetals.provideLangEntries(langConsumer);
		BlazingAdvancements.provideLangEntries(langConsumer);
		BlazingLang.provideLangEntries(langConsumer);
		ItemDescriptions.provideLangEntries(langConsumer);
//		DyeUtil.provideLangEntries(langConsumer); these should already be added by tags convention
		BlazingPonderPlugin.providePonderLang(langConsumer);
	}

}
