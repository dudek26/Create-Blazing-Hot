package com.dudko.blazinghot.data.fabric;

import com.dudko.blazinghot.content.fluids.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.Map;
import java.util.function.BiConsumer;

public class BlazingLangGen {

    public static void generate(RegistrateLangProvider provider) {
        BiConsumer<String, String> langConsumer = provider::add;

        BlazingTags.provideLangEntries(langConsumer);
        CommonTags.provideLangEntries(langConsumer);
        MoltenMetal.provideLangEntries(langConsumer);
        provideDefaultLang("tooltips", langConsumer);
        provideDefaultLang("interface", langConsumer);
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/blazinghot/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

}
