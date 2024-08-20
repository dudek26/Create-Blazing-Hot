package com.dudko.blazinghot.data.lang;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class BlazingItemDescription {

    private final String key;
    private final String summary;
    private final Map<String, String> behaviours;

    public BlazingItemDescription(String key, String summary, Map<String, String> behaviours) {
        this.key = key;
        this.summary = summary;
        this.behaviours = behaviours;
    }

    public BlazingItemDescription register() {
        ItemDescriptions.ALL.add(this);
        return this;
    }

    public String getKey() {
        return BlazingLang.Prefix.ITEM.key + "." + key;
    }

    public void provideLang(BiConsumer<String, String> consumer) {
        String prefix = getKey() + ".tooltip";
        consumer.accept(prefix, key.toUpperCase().replace('_', ' '));
        consumer.accept(prefix + ".summary", summary);

        int index = 0;
        for (Map.Entry<String, String> entry : behaviours.entrySet()) {
            index++;
            consumer.accept(prefix + ".condition" + index, entry.getKey());
            consumer.accept(prefix + ".behaviour" + index, entry.getValue());
        }
    }

    public static Builder builder(String key) {
        return new Builder(key);
    }

    public static class Builder {

        private final String key;
        private String summary;
        private final LinkedHashMap<String, String> behaviours = new LinkedHashMap<>();

        Builder(String key) {
            this.key = key;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        /**
         * @param condition When x...
         * @param behaviour ...y happens
         */
        public Builder addBehaviour(String condition, String behaviour) {
            behaviours.put(condition, behaviour);
            return this;
        }

        public BlazingItemDescription build() {
            return new BlazingItemDescription(key, summary, behaviours);
        }

        /**
         * Shortcut for building and registering the item description
         */
        public BlazingItemDescription register() {
            return build().register();
        }
    }

}
