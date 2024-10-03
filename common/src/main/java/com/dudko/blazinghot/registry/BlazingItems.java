package com.dudko.blazinghot.registry;

import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.EXTINGUISHING;
import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.FOIL;
import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.OXYGEN;
import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.REMOVE_SLOWNESS_0;
import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.REMOVE_SLOWNESS_1;
import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.REMOVE_SLOWNESS_2;
import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.REMOVE_SLOWNESS_ANY;
import static com.dudko.blazinghot.registry.BlazingItems.FoodItemBuilder.tickMinutes;
import static com.dudko.blazinghot.registry.BlazingItems.FoodItemBuilder.tickSeconds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.item.BlazeArrowItem;
import com.dudko.blazinghot.content.item.BlazingFoodItem;
import com.dudko.blazinghot.data.lang.BlazingItemDescription;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.dudko.blazinghot.util.ListUtil;
import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.CombustibleItem;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings({"SameParameterValue", "unused"})
@ParametersAreNonnullByDefault
public class BlazingItems {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static final List<ItemLike> METAL_FOOD = new ArrayList<>();

	static {
		BlazingCreativeTabs.useBaseTab();

		Molds.register();
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
			BLAZE_GOLD_NUGGET =
					taggedIngredient("blaze_gold_nugget", CommonTags.Items.BLAZE_GOLD_NUGGETS.bothTags()),
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

	public static final ItemEntry<Item> BLAZE_WHISK = ingredient("blaze_whisk"),
			STURDY_ALLOY =
					ingredient("sturdy_alloy");

	public static final ItemEntry<Item> NETHER_DOUGH = ingredient("nether_dough");

	public static final ItemEntry<CombustibleItem>
			BLAZE_ROLL =
			REGISTRATE
					.item("blaze_roll", CombustibleItem::new)
					.tag(AllItemTags.BLAZE_BURNER_FUEL_SPECIAL.tag)
					.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.create.blaze_cake"))
					.onRegister(i -> i.setBurnTime(4800))
					.register();

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
			STONE_DUST =
					taggedIngredient("stone_dust", CommonTags.Items.STONE_DUSTS.bothTags()),
			SOUL_DUST =
					taggedIngredient("soul_dust", CommonTags.Items.SOUL_SAND_DUSTS.bothTags());

	public static final ItemEntry<Item> NETHER_COMPOUND = ingredient("nether_compound"),
			NETHER_ESSENCE =
					ingredient("nether_essence");

	public static final ItemEntry<BlazeArrowItem>
			BLAZE_ARROW =
			REGISTRATE
					.item("blaze_arrow", BlazeArrowItem::new)
					.tag(ItemTags.ARROWS)
					.onRegisterAfter(Registries.ITEM,
							v -> ItemDescription.useKey(v, ItemDescriptions.BLAZE_ARROW.getKey()))
					.register();

	public static final ItemEntry<BlazingFoodItem>
			STELLAR_GOLDEN_APPLE =
			new FoodItemBuilder<>("stellar_golden_apple", BlazingFoodItem::new)
					.metalApple()
					.tag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
					.addEffect(MobEffects.ABSORPTION, tickMinutes(1), 2)
					.addEffect(MobEffects.REGENERATION, tickSeconds(20), 1)
					.addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(3))
					.fireResistant()
					.register(),
			IRON_CARROT =
					new FoodItemBuilder<>("iron_carrot", BlazingFoodItem::new)
							.nutrition(5)
							.saturationMod(0.8f)
							.tag(BlazingTags.Items.METAL_CARROTS.tag)
							.register(),
			IRON_APPLE =
					new FoodItemBuilder<>("iron_apple", BlazingFoodItem::new)
							.metalApple()
							.tag(BlazingTags.Items.METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(1))
							.addEffect(MobEffects.DAMAGE_RESISTANCE, tickSeconds(30))
							.register(),
			STELLAR_IRON_APPLE =
					new FoodItemBuilder<>("stellar_iron_apple", BlazingFoodItem::new)
							.metalApple()
							.tag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10))
							.addEffect(MobEffects.DAMAGE_RESISTANCE, tickMinutes(2), 1)
							.register(),
			ENCHANTED_IRON_APPLE =
					new FoodItemBuilder<>("enchanted_iron_apple", p -> new BlazingFoodItem(p, FOIL))
							.enchantedMetalApple()
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2), 2)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
							.addEffect(MobEffects.DAMAGE_RESISTANCE, tickMinutes(3), 2)
							.register(),
			BLAZE_CARROT =
					new FoodItemBuilder<>("blaze_carrot", p -> new BlazingFoodItem(p, EXTINGUISHING))
							.nutrition(6)
							.saturationMod(1.2f)
							.alwaysEat()
							.fireResistant()
							.tag(BlazingTags.Items.METAL_CARROTS.tag)
							.register(),
			BLAZE_APPLE =
					new FoodItemBuilder<>("blaze_apple", p -> new BlazingFoodItem(p, EXTINGUISHING))
							.metalApple()
							.tag(BlazingTags.Items.METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2))
							.addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(5))
							.fireResistant()
							.register(),
			STELLAR_BLAZE_APPLE =
					new FoodItemBuilder<>("stellar_blaze_apple", p -> new BlazingFoodItem(p, EXTINGUISHING))
							.metalApple()
							.tag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10))
							.addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(8))
							.fireResistant()
							.register(),
			ENCHANTED_BLAZE_APPLE =
					new FoodItemBuilder<>("enchanted_blaze_apple", p -> new BlazingFoodItem(p, EXTINGUISHING, FOIL))
							.enchantedMetalApple()
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2), 2)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
							.addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(8))
							.addEffect(MobEffects.DAMAGE_BOOST, tickMinutes(5))
							.fireResistant()
							.register(),
			COPPER_CARROT =
					new FoodItemBuilder<>("copper_carrot", p -> new BlazingFoodItem(p, OXYGEN))
							.nutrition(3)
							.saturationMod(1f)
							.alwaysEat()
							.description(ItemDescriptions.OXYGEN_FOOD)
							.tag(BlazingTags.Items.METAL_CARROTS.tag)
							.register(),
			COPPER_APPLE =
					new FoodItemBuilder<>("copper_apple", BlazingFoodItem::new)
							.metalApple()
							.tag(BlazingTags.Items.METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickSeconds(30))
							.addEffect(MobEffects.WATER_BREATHING, tickMinutes(1))
							.register(),
			STELLAR_COPPER_APPLE =
					new FoodItemBuilder<>("stellar_copper_apple", BlazingFoodItem::new)
							.metalApple()
							.tag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10))
							.addEffect(MobEffects.WATER_BREATHING, tickMinutes(5))
							.register(),
			ENCHANTED_COPPER_APPLE =
					new FoodItemBuilder<>("enchanted_copper_apple", p -> new BlazingFoodItem(p, FOIL))
							.enchantedMetalApple()
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2), 2)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
							.addEffect(MobEffects.WATER_BREATHING, tickMinutes(8))
							.addEffect(MobEffects.DOLPHINS_GRACE, tickSeconds(30))
							.register(),
			ZINC_CARROT =
					new FoodItemBuilder<>("zinc_carrot", p -> new BlazingFoodItem(p, REMOVE_SLOWNESS_0))
							.nutrition(5)
							.saturationMod(0.8f)
							.alwaysEat()
							.description(ItemDescriptions.SLOWNESS_REMOVING_FOOD_0)
							.tag(BlazingTags.Items.METAL_CARROTS.tag)
							.register(),
			ZINC_APPLE =
					new FoodItemBuilder<>("zinc_apple", p -> new BlazingFoodItem(p, REMOVE_SLOWNESS_1))
							.metalApple()
							.tag(BlazingTags.Items.METAL_APPLES.tag)
							.description(ItemDescriptions.SLOWNESS_REMOVING_FOOD_1)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(1))
							.addEffect(MobEffects.MOVEMENT_SPEED, tickSeconds(30))
							.register(),
			STELLAR_ZINC_APPLE =
					new FoodItemBuilder<>("stellar_zinc_apple", p -> new BlazingFoodItem(p, REMOVE_SLOWNESS_2))
							.metalApple()
							.tag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
							.description(ItemDescriptions.SLOWNESS_REMOVING_FOOD_2)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10))
							.addEffect(MobEffects.MOVEMENT_SPEED, tickMinutes(3), 1)
							.register(),
			ENCHANTED_ZINC_APPLE =
					new FoodItemBuilder<>("enchanted_zinc_apple",
							p -> new BlazingFoodItem(p, REMOVE_SLOWNESS_ANY, FOIL))
							.enchantedMetalApple()
							.description(ItemDescriptions.SLOWNESS_REMOVING_FOOD_ANY)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2), 2)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
							.addEffect(MobEffects.SLOW_FALLING, tickMinutes(2))
							.addEffect(MobEffects.MOVEMENT_SPEED, tickMinutes(5), 1)
							.register(),
			BRASS_CARROT =
					new FoodItemBuilder<>("brass_carrot", BlazingFoodItem::new)
							.nutrition(6)
							.saturationMod(1f)
							.tag(BlazingTags.Items.METAL_CARROTS.tag)
							.alwaysEat()
							.addEffect(MobEffects.DIG_SPEED, tickSeconds(10))
							.register(),
			BRASS_APPLE =
					new FoodItemBuilder<>("brass_apple", BlazingFoodItem::new)
							.metalApple()
							.tag(BlazingTags.Items.METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2))
							.addEffect(MobEffects.DIG_SPEED, tickMinutes(2))
							.register(),
			STELLAR_BRASS_APPLE =
					new FoodItemBuilder<>("stellar_brass_apple", BlazingFoodItem::new)
							.metalApple()
							.tag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
							.addEffect(MobEffects.ABSORPTION, tickMinutes(1), 1)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
							.addEffect(MobEffects.DIG_SPEED, tickMinutes(5))
							.register(),
			ENCHANTED_BRASS_APPLE =
					new FoodItemBuilder<>("enchanted_brass_apple", p -> new BlazingFoodItem(p, FOIL))
							.enchantedMetalApple()
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2), 2)
							.addEffect(MobEffects.REGENERATION, tickSeconds(10), 1)
							.addEffect(MobEffects.DIG_SPEED, tickMinutes(5), 1)
							.addEffect(MobEffects.JUMP, tickMinutes(5), 1)
							.register(),
			ENCHANTED_NETHERITE_APPLE =
					new FoodItemBuilder<>("enchanted_netherite_apple", p -> new BlazingFoodItem(p, EXTINGUISHING, FOIL))
							.enchantedMetalApple()
							.addEffect(MobEffects.ABSORPTION, tickMinutes(2), 3)
							.addEffect(MobEffects.REGENERATION, tickSeconds(20), 1)
							.addEffect(MobEffects.DAMAGE_RESISTANCE, tickMinutes(5), 1)
							.addEffect(MobEffects.DAMAGE_BOOST, tickMinutes(5))
							.addEffect(MobEffects.FIRE_RESISTANCE, tickMinutes(8))
							.fireResistant()
							.register();

	public static final ItemEntry<SequencedAssemblyItem>
			HEAVY_STELLAR_IRON_APPLE =
			sequencedIngredient("heavy_stellar_iron_apple", Rarity.RARE),
			GILDED_STELLAR_GOLDEN_APPLE =
					sequencedIngredient("gilded_stellar_golden_apple", Rarity.RARE),
			BURNING_STELLAR_BLAZE_APPLE =
					sequencedIngredient("burning_stellar_blaze_apple", Rarity.RARE),
			COATED_STELLAR_COPPER_APPLE =
					sequencedIngredient("coated_stellar_copper_apple", Rarity.RARE),
			GALVANIZED_STELLAR_ZINC_APPLE =
					sequencedIngredient("galvanized_stellar_zinc_apple", Rarity.RARE),
			BRASSY_STELLAR_BRASS_APPLE =
					sequencedIngredient("brassy_stellar_brass_apple", Rarity.RARE),
			ANCIENT_ENCHANTED_APPLE =
					sequencedIngredient("ancient_enchanted_apple", Rarity.EPIC);

	public static void register() {
		METAL_FOOD.addAll(List.of(Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE));
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

		protected final FoodItemBuilder<T> metalApple() {
			return nutrition(4).saturationMod(1.1f).alwaysEat().rarity(Rarity.RARE);
		}

		protected final FoodItemBuilder<T> enchantedMetalApple() {
			return nutrition(4)
					.saturationMod(1.1f)
					.alwaysEat()
					.rarity(Rarity.EPIC)
					.tag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag);
		}

		protected final FoodItemBuilder<T> description(String description) {
			this.description = description;
			return this;
		}

		protected final FoodItemBuilder<T> description(BlazingItemDescription description) {
			return description(description.getKey());
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

		public enum Descriptions {
			EXTINGUISHING("item.blazinghot.extinguishing_food"); // automatically added to extinguishing items

			public final String key;

			Descriptions(String key) {
				this.key = key;
			}

		}


		private Item.Properties finishProperties() {
			return properties.food(foodProperties.build());
		}

		/**
		 * Builds the item.
		 *
		 * @return The item builder.
		 * @apiNote This does not register the item to the game. Use for other modifications.
		 * @see #register()
		 */
		protected ItemBuilder<T, CreateRegistrate> build() {
			ItemBuilder<T, CreateRegistrate>
					builder =
					REGISTRATE
							.item(name, factory)
							.properties(p -> finishProperties())
							.tag(CommonTags.Items.FOODS.bothTags());
			if (tags.length > 0) builder.tag(tags);

			builder.onRegisterAfter(Registries.ITEM, c -> {
				if (c instanceof BlazingFoodItem food) {
					if (food.isExtinguishing()) ItemDescription.useKey(c, ItemDescriptions.EXTINGUISHING_FOOD.getKey());
					if (food.getOxygen() > 0) ItemDescription.useKey(c, ItemDescriptions.OXYGEN_FOOD.getKey());
					if (food.getMaxRemovedSlowness() >= 0)
						ItemDescription.useKey(c, food.getRemovedSlownessDescription());
				}
			});

			if (description != null)
				builder.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, description));

			if (ListUtil.containsAny(Arrays.stream(tags).toList(),
					BlazingTags.Items.METAL_CARROTS.tag,
					BlazingTags.Items.METAL_APPLES.tag,
					BlazingTags.Items.STELLAR_METAL_APPLES.tag,
					BlazingTags.Items.ENCHANTED_METAL_APPLES.tag)) {
				builder.onRegisterAfter(Registries.ITEM, METAL_FOOD::add);
			}
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
