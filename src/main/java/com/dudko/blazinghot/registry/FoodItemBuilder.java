package com.dudko.blazinghot.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dudko.blazinghot.BlazingHot.REGISTRATE;

@SuppressWarnings("unused")
public class FoodItemBuilder<T extends Item> {

    protected static int tickSeconds(int seconds) {
        return seconds * 20;
    }

    protected static int tickMinutes(int minutes) {
        return tickSeconds(minutes * 60);
    }

    private Item.Properties properties = new Item.Properties();
    private FoodProperties.Builder foodProperties = new FoodProperties.Builder();
    private final String name;
    private String description;
    private final NonNullFunction<Item.Properties, T> factory;
    private TagKey<Item>[] tags;

    @SuppressWarnings("unchecked")
    protected FoodItemBuilder(String name, NonNullFunction<Item.Properties, T> factory) {
        this.name = name;
        this.factory = factory;
        this.tags = List.of(BlazingTags.Items.FOODS.tag).toArray(new TagKey[1]);
    }

    protected FoodItemBuilder<T> maxStackSize(int maxStackSize) {
        this.properties = properties.stacksTo(maxStackSize);
        return this;
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    protected final FoodItemBuilder<T> tag(TagKey<Item>... tags) {
        List<TagKey<Item>> tagList = new ArrayList<>(Arrays.asList(tags));
        TagKey<Item>[] newTags = new TagKey[tagList.size()];
        this.tags = tagList.toArray(newTags);
        return this;
    }

    protected final FoodItemBuilder<T> description(String description) {
        this.description = description;
        return this;
    }

    protected FoodItemBuilder<T> rarity(Rarity rarity) {
        properties = properties.rarity(rarity);
        return this;
    }

    protected FoodItemBuilder<T> nutrition(int nutrition) {
        foodProperties = foodProperties.nutrition(nutrition);
        return this;
    }

    protected FoodItemBuilder<T> saturationMod(float saturationModifier) {
        foodProperties = foodProperties.saturationMod(saturationModifier);
        return this;
    }

    protected FoodItemBuilder<T> meat() {
        foodProperties = foodProperties.meat();
        return this;
    }

    protected FoodItemBuilder<T> alwaysEat() {
        foodProperties = foodProperties.alwaysEat();
        return this;
    }

    protected FoodItemBuilder<T> fast() {
        foodProperties = foodProperties.fast();
        return this;
    }

    protected FoodItemBuilder<T> addEffect(MobEffect effect, int duration, int amplifier) {
        return addEffect(effect, duration, amplifier, 1);
    }

    protected FoodItemBuilder<T> addEffect(MobEffect effect, int duration) {
        return addEffect(effect, duration, 0);
    }

    protected FoodItemBuilder<T> addEffect(MobEffectInstance effect, float probability) {
        foodProperties = foodProperties.effect(effect, probability);
        return this;
    }

    protected FoodItemBuilder<T> addEffect(MobEffect effect, int duration, int amplifier, float probability) {
        return addEffect(new MobEffectInstance(effect, duration, amplifier), probability);
    }


    private Item.Properties finishProperties() {
        return properties.food(foodProperties.build());
    }

    /**
     * Builds the item.
     *
     * @return The item builder.
     * @apiNote This does not register the item to the game. Use for other modifications, such as a custom tooltip.
     * @see #register()
     */
    protected ItemBuilder<T, CreateRegistrate> build() {
        ItemBuilder<T, CreateRegistrate> builder =
                REGISTRATE.item(name, factory).properties(p -> finishProperties()).tag(BlazingTags.Items.FOODS.tag);
        if (tags.length > 0) builder.tag(tags);
        if (description != null) builder.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, description));
        return builder;
    }

    /**
     * Builds and registers the item.
     *
     * @return The item entry.
     * @see #build()
     */
    protected ItemEntry<T> register() {
        return build().register();
    }

}
