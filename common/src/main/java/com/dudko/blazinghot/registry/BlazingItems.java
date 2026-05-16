package com.dudko.blazinghot.registry;

import static com.dudko.blazinghot.content.item.food.StandardFoodBuilders.apple;
import static com.dudko.blazinghot.content.item.food.StandardFoodBuilders.carrot;
import static com.dudko.blazinghot.content.item.food.StandardFoodBuilders.enchantedApple;
import static com.dudko.blazinghot.content.item.food.StandardFoodBuilders.stellarApple;
import static com.dudko.blazinghot.foundation.multiloader.BlazingBuilderTransformers.existingParent;
import static com.dudko.blazinghot.foundation.multiloader.BlazingBuilderTransformers.handheld;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.item.BlazeArrowItem;
import com.dudko.blazinghot.content.item.food.BlazingFoodItem;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.simibubi.create.api.data.datamaps.BlazeBurnerFuel;
import com.simibubi.create.api.registry.CreateDataMaps;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
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
			BlazingTags.Items.BLAZE_GOLD_INGOTS.tag(),
			BlazingTags.Items.INGOTS.tag(),
			ItemTags.BEACON_PAYMENT_ITEMS),
		BLAZE_GOLD_NUGGET =
			taggedIngredient("blaze_gold_nugget",
				BlazingTags.Items.BLAZE_GOLD_NUGGETS.tag(),
				BlazingTags.Items.NUGGETS.tag()),
		BLAZE_GOLD_SHEET =
			taggedIngredient("blaze_gold_sheet",
				BlazingTags.Items.BLAZE_GOLD_PLATES.tag(),
				BlazingTags.Items.PLATES.tag()),
		BLAZE_GOLD_ROD =
			REGISTRATE
				.item("blaze_gold_rod", Item::new)
				.tag(BlazingTags.Items.BLAZE_GOLD_RODS.tag())
				.tag(BlazingTags.Items.RODS.tag())
				.transform(handheld())
				.register();

	public static final ItemEntry<Item> BLAZE_WHISK = ingredient("blaze_whisk"),
		STURDY_ALLOY =
			taggedIngredient("sturdy_alloy", BlazingTags.Items.INGOTS.tag());

	public static final ItemEntry<Item> NETHER_DOUGH = ingredient("nether_dough");

	public static final ItemEntry<Item>
		BLAZE_ROLL =
		REGISTRATE
			.item("blaze_roll", Item::new)
			.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.create.blaze_cake"))
			.dataMap(CreateDataMaps.SUPERHEATED_BLAZE_BURNER_FUELS, new BlazeBurnerFuel(2400))
			.burnTime(4800)
			.register();

	public static final ItemEntry<SequencedAssemblyItem>
		INCOMPLETE_BLAZE_MIXER =
		REGISTRATE
			.item("incomplete_blaze_mixer", SequencedAssemblyItem::new)
			.transform(existingParent(BlazingHot.asResource("block/blaze_mixer/block")))
			.register();

	public static final ItemEntry<Item>
		NETHERRACK_DUST =
		taggedIngredient("netherrack_dust", BlazingTags.Items.NETHERRACK_DUSTS.tag()),
		STONE_DUST =
			taggedIngredient("stone_dust", BlazingTags.Items.STONE_DUSTS.tag()),
		SOUL_DUST =
			taggedIngredient("soul_dust", BlazingTags.Items.SOUL_SAND_DUSTS.tag());

	public static final ItemEntry<Item> NETHER_COMPOUND = ingredient("nether_compound"),
		CRIMSON_ESSENCE =
			REGISTRATE.item("crimson_essence", Item::new).register();

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
		stellarApple("golden",
			b -> b
				.effect(MobEffects.ABSORPTION, 20 * 60)
				.effect(MobEffects.REGENERATION, 20 * 20, 1)
				.effect(MobEffects.FIRE_RESISTANCE, 20 * 60 * 3)),
		IRON_CARROT =
			carrot("iron", b -> b.nutrition(5).saturation(0.8f)),
		IRON_APPLE =
			apple("iron",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60)
					.effect(MobEffects.DAMAGE_RESISTANCE, 20 * 30)),
		STELLAR_IRON_APPLE =
			stellarApple("iron",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60, 1)
					.effect(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 2, 1)
					.effect(MobEffects.REGENERATION, 20 * 10)),
		ENCHANTED_IRON_APPLE =
			enchantedApple("iron",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2, 2)
					.effect(MobEffects.REGENERATION, 20 * 10, 1)
					.effect(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 3, 2)),
		BLAZE_CARROT =
			carrot("blaze", b -> b.nutrition(6).saturation(1.2f).alwaysEat().fireResistant().extinguish()),
		BLAZE_APPLE =
			apple("blaze",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2)
					.effect(MobEffects.FIRE_RESISTANCE, 20 * 60 * 5)
					.fireResistant()
					.extinguish()),
		STELLAR_BLAZE_APPLE =
			stellarApple("blaze",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2, 1)
					.effect(MobEffects.REGENERATION, 20 * 10)
					.effect(MobEffects.FIRE_RESISTANCE, 20 * 60 * 8)
					.fireResistant()
					.extinguish()),
		ENCHANTED_BLAZE_APPLE =
			enchantedApple("blaze",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2, 2)
					.effect(MobEffects.REGENERATION, 20 * 10, 1)
					.effect(MobEffects.FIRE_RESISTANCE, 20 * 60 * 8)
					.effect(MobEffects.DAMAGE_BOOST, 20 * 60 * 5)
					.fireResistant()
					.extinguish()),
		COPPER_CARROT =
			carrot("copper", b -> b.nutrition(3).saturation(1f).alwaysEat().addOxygen(50)),
		COPPER_APPLE =
			apple("copper",
				b -> b.effect(MobEffects.ABSORPTION, 20 * 30).effect(MobEffects.WATER_BREATHING, 20 * 60)),
		STELLAR_COPPER_APPLE =
			stellarApple("copper",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60, 1)
					.effect(MobEffects.WATER_BREATHING, 20 * 60 * 5)
					.effect(MobEffects.REGENERATION, 20 * 10)),
		ENCHANTED_COPPER_APPLE =
			enchantedApple("copper",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2, 2)
					.effect(MobEffects.REGENERATION, 20 * 10, 1)
					.effect(MobEffects.WATER_BREATHING, 20 * 60 * 8)
					.effect(MobEffects.DOLPHINS_GRACE, 20 * 30)),
		ZINC_CARROT =
			carrot("zinc", b -> b.nutrition(5).saturation(0.8f).alwaysEat().removeSlowness(0)),
		ZINC_APPLE =
			apple("zinc",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60)
					.effect(MobEffects.MOVEMENT_SPEED, 20 * 30)
					.removeSlowness(1)),
		STELLAR_ZINC_APPLE =
			stellarApple("zinc",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60, 1)
					.effect(MobEffects.MOVEMENT_SPEED, 20 * 60 * 3, 1)
					.effect(MobEffects.REGENERATION, 20 * 10)
					.removeSlowness(2)),
		ENCHANTED_ZINC_APPLE =
			enchantedApple("zinc",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2, 2)
					.effect(MobEffects.REGENERATION, 20 * 10, 1)
					.effect(MobEffects.SLOW_FALLING, 20 * 60 * 2)
					.effect(MobEffects.MOVEMENT_SPEED, 20 * 60 * 5, 1)
					.removeSlowness()),
		BRASS_CARROT =
			carrot("brass",
				b -> b.nutrition(6).saturation(1f).alwaysEat().effect(MobEffects.DIG_SPEED, 20 * 10)),
		BRASS_APPLE =
			apple("brass",
				b -> b.effect(MobEffects.ABSORPTION, 20 * 60).effect(MobEffects.DIG_SPEED, 20 * 60 * 2)),
		STELLAR_BRASS_APPLE =
			stellarApple("brass",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60, 1)
					.effect(MobEffects.DIG_SPEED, 20 * 60 * 5)
					.effect(MobEffects.REGENERATION, 20 * 10, 1)),
		ENCHANTED_BRASS_APPLE =
			enchantedApple("brass",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2, 2)
					.effect(MobEffects.REGENERATION, 20 * 10, 1)
					.effect(MobEffects.JUMP, 20 * 60 * 5, 1)
					.effect(MobEffects.DIG_SPEED, 20 * 60 * 5, 1)),
		ENCHANTED_NETHERITE_APPLE =
			enchantedApple("netherite",
				b -> b
					.effect(MobEffects.ABSORPTION, 20 * 60 * 2, 3)
					.effect(MobEffects.REGENERATION, 20 * 10, 1)
					.effect(MobEffects.FIRE_RESISTANCE, 20 * 60 * 8)
					.effect(MobEffects.DAMAGE_BOOST, 20 * 60 * 5)
					.effect(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 5, 1)
					.fireResistant()
					.extinguish());

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

}
