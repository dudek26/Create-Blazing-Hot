package com.dudko.blazinghot.data.lang;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.util.DyeUtil;
import com.tterrag.registrate.providers.RegistrateLangProvider;

public class BlazingLangGen {

	public static void generate(RegistrateLangProvider provider) {
		BiConsumer<String, String> langConsumer = provider::add;

		BlazingTags.provideLangEntries(langConsumer);
		CommonTags.provideLangEntries(langConsumer);
		MoltenMetal.provideLangEntries(langConsumer);
		BlazingAdvancements.provideLangEntries(langConsumer);
		BlazingLang.provideLangEntries(langConsumer);
		ItemDescriptions.provideLangEntries(langConsumer);
		DyeUtil.provideLangEntries(langConsumer);
	}

}
