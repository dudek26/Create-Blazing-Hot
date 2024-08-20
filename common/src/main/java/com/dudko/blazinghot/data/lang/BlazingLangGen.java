package com.dudko.blazinghot.data.lang;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.function.BiConsumer;

public class BlazingLangGen {

    public static void generate(RegistrateLangProvider provider) {
        BiConsumer<String, String> langConsumer = provider::add;

        BlazingTags.provideLangEntries(langConsumer);
        CommonTags.provideLangEntries(langConsumer);
        MoltenMetal.provideLangEntries(langConsumer);
        BlazingAdvancements.provideLangEntries(langConsumer);
        BlazingLang.provideLangEntries(langConsumer);
        ItemDescriptions.provideLangEntries(langConsumer);
    }

}
