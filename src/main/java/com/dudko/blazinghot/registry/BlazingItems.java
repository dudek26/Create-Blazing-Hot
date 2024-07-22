package com.dudko.blazinghot.registry;


import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.item.BlazeArrowItem;
import com.dudko.blazinghot.content.item.ExtinguishingItem;
import com.dudko.blazinghot.content.item.FoilableItem;
import com.dudko.blazinghot.content.item.Foods;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static com.dudko.blazinghot.BlazingHot.REGISTRATE;
import static com.dudko.blazinghot.registry.FoodItemBuilder.tickMinutes;
import static com.dudko.blazinghot.registry.FoodItemBuilder.tickSeconds;
import static com.simibubi.create.AllTags.AllItemTags.PLATES;

@SuppressWarnings({"SameParameterValue", "unused"})
public class BlazingItems {

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

    private static ItemEntry<Item> food(String name, Foods food, Rarity rarity) {
        return food(name, food, rarity, false);
    }

    private static ItemEntry<Item> food(String name, Foods food, Rarity rarity, boolean foil) {
        return REGISTRATE
                .item(name, Item::new)
                .properties(p -> p.rarity(rarity).food(food.foodProperties))
                .tag(BlazingTags.Items.FOODS.tag)
                .register();
    }

    private static ItemEntry<Item> food(String name, Foods food) {
        return food(name, food, Rarity.COMMON);
    }

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new).tag(tags).register();
    }

    public static final ItemEntry<Item> BLAZE_GOLD_INGOT =
            taggedIngredient("blaze_gold_ingot", BlazingTags.commonItemTag("blaze_gold_ingots"),
                             ItemTags.BEACON_PAYMENT_ITEMS), BLAZE_GOLD_NUGGET =
            taggedIngredient("blaze_gold_nugget", BlazingTags.commonItemTag("blaze_gold_nuggets")), BLAZE_GOLD_SHEET =
            taggedIngredient("blaze_gold_sheet", BlazingTags.commonItemTag("blaze_gold_plates"), PLATES.tag),
            BLAZE_GOLD_ROD = REGISTRATE
                    .item("blaze_gold_rod", Item::new)
                    .tag(BlazingTags.commonItemTag("blaze_gold_rods"))
                    .model((c, p) -> p.handheld(c))
                    .register();

    public static final ItemEntry<Item> NETHERRACK_DUST =
            taggedIngredient("netherrack_dust", BlazingTags.commonItemTag("netherrack_dusts")), STONE_DUST =
            taggedIngredient("stone_dust", BlazingTags.commonItemTag("stone_dusts")), SOUL_DUST =
            taggedIngredient("soul_dust", BlazingTags.commonItemTag("soul_sand_dusts"));

    public static final ItemEntry<Item> NETHER_COMPOUND = ingredient("nether_compound"), NETHER_ESSENCE =
            ingredient("nether_essence");

    public static final ItemEntry<ExtinguishingItem> BLAZE_CARROT = REGISTRATE
            .item("blaze_carrot", ExtinguishingItem::new)
            .properties(p -> p.food(Foods.BLAZE_CARROT.foodProperties))
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.extinguishing_food"))
            .tag(BlazingTags.Items.FOODS.tag)
            .register();

    public static final ItemEntry<Item> STELLAR_GOLDEN_APPLE = REGISTRATE
            .item("stellar_golden_apple", Item::new)
            .properties(p -> p.food(Foods.STELLAR_GOLDEN_APPLE.foodProperties).rarity(Rarity.RARE).fireResistant())
            .tag(ItemTags.PIGLIN_LOVED)
            .tag(BlazingTags.Items.FOODS.tag)
            .register();

    public static final ItemEntry<SequencedAssemblyItem> GILDED_STELLAR_GOLDEN_APPLE = REGISTRATE
            .item("gilded_stellar_golden_apple", SequencedAssemblyItem::new)
            .properties(p -> p.rarity(Rarity.RARE))
            .register();

    public static final ItemEntry<ExtinguishingItem> BLAZE_APPLE = REGISTRATE
            .item("blaze_apple", ExtinguishingItem::new)
            .tag(BlazingTags.Items.FOODS.tag)
            .properties(p -> p.food(Foods.BLAZE_APPLE.foodProperties).rarity(Rarity.RARE).fireResistant())
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.extinguishing_food"))
            .register();

    public static final ItemEntry<ExtinguishingItem> STELLAR_BLAZE_APPLE = REGISTRATE
            .item("stellar_blaze_apple", ExtinguishingItem::new)
            .tag(BlazingTags.Items.FOODS.tag)
            .properties(p -> p.food(Foods.STELLAR_BLAZE_APPLE.foodProperties).rarity(Rarity.RARE).fireResistant())
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.extinguishing_food"))
            .register();

    public static final ItemEntry<SequencedAssemblyItem> BURNING_STELLAR_BLAZE_APPLE = REGISTRATE
            .item("burning_stellar_blaze_apple", SequencedAssemblyItem::new)
            .properties(p -> p.rarity(Rarity.RARE))
            .register();

    public static final ItemEntry<ExtinguishingItem> ENCHANTED_BLAZE_APPLE = REGISTRATE
            .item("enchanted_blaze_apple", p -> new ExtinguishingItem(p, true))
            .tag(BlazingTags.Items.FOODS.tag)
            .properties(p -> p.food(Foods.ENCHANTED_BLAZE_APPLE.foodProperties).rarity(Rarity.EPIC).fireResistant())
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.extinguishing_food"))
            .register();

    public static final ItemEntry<BlazeArrowItem> BLAZE_ARROW = REGISTRATE
            .item("blaze_arrow", BlazeArrowItem::new)
            .tag(ItemTags.ARROWS)
            .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.blaze_arrow"))
            .register();

    public static final ItemEntry<Item> IRON_CARROT =
            new FoodItemBuilder<>("iron_carrot", Item::new).nutrition(5).saturationMod(0.8f).register(), IRON_APPLE =
            new FoodItemBuilder<>("iron_apple", Item::new)
                    .rarity(Rarity.RARE)
                    .nutrition(4)
                    .alwaysEat()
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(1))
                    .addEffect(MobEffects.DAMAGE_RESISTANCE, tickSeconds(30))
                    .register(), STELLAR_IRON_APPLE = new FoodItemBuilder<>("stellar_iron_apple", Item::new)
            .rarity(Rarity.RARE)
            .nutrition(4)
            .alwaysEat()
            .saturationMod(1.1f)
            .addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
            .addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
            .addEffect(MobEffects.DAMAGE_RESISTANCE, tickMinutes(2), 1)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> HEAVY_STELLAR_IRON_APPLE =
            sequencedIngredient("heavy_stellar_iron_apple", Rarity.RARE);
    public static final ItemEntry<FoilableItem> ENCHANTED_IRON_APPLE =
            new FoodItemBuilder<>("enchanted_iron_apple", FoilableItem::new)
                    .rarity(Rarity.EPIC)
                    .nutrition(4)
                    .alwaysEat()
                    .saturationMod(1.1f)
                    .addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
                    .addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
                    .addEffect(MobEffects.DAMAGE_RESISTANCE, tickMinutes(2), 2)
                    .register();

    public static void register() {
        BlazingHot.LOGGER.info("Registering Mod Items for " + BlazingHot.NAME);
    }

    public static void setupCreativeTab() {
        REGISTRATE.setCreativeTab(BlazingCreativeTabs.getKey());
    }
}
