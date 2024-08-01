package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.item.BlazeArrowItem;
import com.dudko.blazinghot.content.item.BlazingFoodItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dudko.blazinghot.content.item.BlazingFoodItem.BProperties.EXTINGUISHING;
import static com.dudko.blazinghot.content.item.BlazingFoodItem.BProperties.FOIL;
import static com.dudko.blazinghot.registry.BlazingItems.FoodItemBuilder.tickMinutes;
import static com.dudko.blazinghot.registry.BlazingItems.FoodItemBuilder.tickSeconds;

@SuppressWarnings({"SameParameterValue", "unused"})
public class BlazingItems {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    static {
        setupCreativeTab();
    }

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new).register();
    }

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return sequencedIngredient(name, Rarity.COMMON);
    }

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name, Rarity rarity) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new).properties(p -> p.rarity(rarity)).register();
    }

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new).tag(tags).register();
    }

    public static final ItemEntry<Item>
            BLAZE_GOLD_INGOT =
            taggedIngredient("blaze_gold_ingot",
                             CommonTags.Items.BLAZE_GOLD_INGOTS.forge,
                             CommonTags.Items.BLAZE_GOLD_INGOTS.fabric,
                             ItemTags.BEACON_PAYMENT_ITEMS),

    BLAZE_GOLD_NUGGET = taggedIngredient("blaze_gold_nugget", CommonTags.Items.BLAZE_GOLD_NUGGETS.bothTags()),

    BLAZE_GOLD_SHEET =
            taggedIngredient("blaze_gold_sheet",
                             CommonTags.Items.BLAZE_GOLD_PLATES.fabric,
                             CommonTags.Items.BLAZE_GOLD_PLATES.forge,
                             CommonTags.Items.PLATES.forge,
                             CommonTags.Items.PLATES.fabric),

    BLAZE_GOLD_ROD =
            REGISTRATE
                    .item("blaze_gold_rod", Item::new)
                    .tag(CommonTags.Items.BLAZE_GOLD_RODS.fabric)
                    .model((c, p) -> p.handheld(c))
                    .register();

    public static final ItemEntry<Item> BLAZE_WHISK = ingredient("blaze_whisk");

    public static final ItemEntry<SequencedAssemblyItem>
            INCOMPLETE_BLAZE_MIXER =
            REGISTRATE
                    .item("incomplete_blaze_mixer", SequencedAssemblyItem::new)
                    .model((c, p) -> p.withExistingParent(c.getName(),
                                                          BlazingHot.asResource("block/blaze_mixer/block")))
                    .register();

    public static final ItemEntry<Item>
            NETHERRACK_DUST =
            taggedIngredient("netherrack_dust", CommonTags.Items.NETHERRACK_DUSTS.bothTags()),

    STONE_DUST = taggedIngredient("stone_dust", CommonTags.Items.STONE_DUSTS.bothTags()),

    SOUL_DUST = taggedIngredient("soul_dust", CommonTags.Items.SOUL_SAND_DUSTS.bothTags());

    public static final ItemEntry<Item> NETHER_COMPOUND = ingredient("nether_compound"),
            NETHER_ESSENCE =
                    ingredient("nether_essence");

    public static final ItemEntry<BlazeArrowItem>
            BLAZE_ARROW =
            REGISTRATE
                    .item("blaze_arrow", BlazeArrowItem::new)
                    .tag(ItemTags.ARROWS)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.blaze_arrow"))
                    .register();

    public static final ItemEntry<Item>
            STELLAR_GOLDEN_APPLE =
            new FoodItemBuilder<>("stellar_golden_apple", Item::new)
                    .alwaysEat()
                    .nutrition(4)
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(2), 1)
                    .addEffect(MobEffects.REGENERATION, tickSeconds(20), 1)
                    .addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(5))
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .register(),

    IRON_CARROT = new FoodItemBuilder<>("iron_carrot", Item::new).nutrition(5).saturationMod(0.8f).register(),

    IRON_APPLE =
            new FoodItemBuilder<>("iron_apple", Item::new)
                    .rarity(Rarity.RARE)
                    .nutrition(4)
                    .alwaysEat()
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(1))
                    .addEffect(MobEffects.DAMAGE_RESISTANCE, tickSeconds(30))
                    .register(),

    STELLAR_IRON_APPLE =
            new FoodItemBuilder<>("stellar_iron_apple", Item::new)
                    .rarity(Rarity.RARE)
                    .nutrition(4)
                    .alwaysEat()
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
                    .addEffect(MobEffects.REGENERATION, tickSeconds(10))
                    .addEffect(MobEffects.DAMAGE_RESISTANCE, tickMinutes(2), 1)
                    .register();
    public static final ItemEntry<BlazingFoodItem>
            ENCHANTED_IRON_APPLE =
            new FoodItemBuilder<>("enchanted_iron_apple", p -> new BlazingFoodItem(p, FOIL))
                    .rarity(Rarity.EPIC)
                    .nutrition(4)
                    .alwaysEat()
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
                    .addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
                    .addEffect(MobEffects.DAMAGE_RESISTANCE, tickMinutes(3), 2)
                    .register();
    public static final ItemEntry<BlazingFoodItem>
            BLAZE_CARROT =
            new FoodItemBuilder<>("blaze_carrot", p -> new BlazingFoodItem(p, EXTINGUISHING))
                    .nutrition(6)
                    .saturationMod(1.2f)
                    .alwaysEat()
                    .fireResistant()
                    .description("item.blazinghot.extinguishing_food")
                    .register(),

    BLAZE_APPLE =
            new FoodItemBuilder<>("blaze_apple", BlazingFoodItem::new)
                    .alwaysEat()
                    .description("item.blazinghot.extinguishing_food")
                    .nutrition(4)
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(2))
                    .addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
                    .addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(5))
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .register(),

    STELLAR_BLAZE_APPLE =
            new FoodItemBuilder<>("stellar_blaze_apple", p -> new BlazingFoodItem(p, EXTINGUISHING))
                    .alwaysEat()
                    .description("item.blazinghot.extinguishing_food")
                    .nutrition(4)
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(2))
                    .addEffect(MobEffects.REGENERATION, tickSeconds(20), 1)
                    .addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(8))
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .register(),

    ENCHANTED_BLAZE_APPLE =
            new FoodItemBuilder<>("enchanted_blaze_apple", p -> new BlazingFoodItem(p, EXTINGUISHING, FOIL))
                    .alwaysEat()
                    .description("item.blazinghot.extinguishing_food")
                    .nutrition(4)
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(2), 2)
                    .addEffect(MobEffects.REGENERATION, tickSeconds(20), 1)
                    .addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(8))
                    .addEffect(MobEffects.DAMAGE_BOOST, tickMinutes(5))
                    .rarity(Rarity.EPIC)
                    .fireResistant()
                    .register();

    public static final ItemEntry<SequencedAssemblyItem>
            HEAVY_STELLAR_IRON_APPLE =
            sequencedIngredient("heavy_stellar_iron_apple", Rarity.RARE),
            GILDED_STELLAR_GOLDEN_APPLE =
                    sequencedIngredient("gilded_stellar_golden_apple", Rarity.RARE),
            BURNING_STELLAR_BLAZE_APPLE =
                    sequencedIngredient("burning_stellar_blaze_apple", Rarity.RARE);


    public static void register() {
    }

    public static void setupCreativeTab() {
//        REGISTRATE.setCreativeTab(BlazingCreativeTabs.getBaseTabKey());
    }

    @SuppressWarnings("unused")
    public static class FoodItemBuilder<T extends Item> {

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
            this.tags = List.of(CommonTags.Items.FOODS.bothTags()).toArray(new TagKey[1]);
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

        protected FoodItemBuilder<T> fireResistant() {
            properties = properties.fireResistant();
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
         * @apiNote This does not init the item to the game. Use for other modifications, such as a custom tooltip.
         * @see #register()
         */
        protected ItemBuilder<T, CreateRegistrate> build() {
            ItemBuilder<T, CreateRegistrate>
                    builder =
                    REGISTRATE.item(name, factory).properties(p -> finishProperties()).tag(CommonTags.Items.FOODS.bothTags());
            if (tags.length > 0) builder.tag(tags);
            if (description != null)
                builder.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, description));
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
}