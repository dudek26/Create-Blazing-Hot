package com.dudko.blazinghot.content.item.food;

import java.util.function.UnaryOperator;

import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Rarity;

public class StandardFoodBuilders {

	public static ItemEntry<BlazingFoodItem> carrot(String baseName, UnaryOperator<BlazingFood.Builder> builder) {
		return BlazingFood.create(baseName + "_carrot",
				b -> builder.apply(b
						.tags(BlazingTags.Items.METAL_CARROTS.tag())
						.onRegisterAfter(BlazingItems.METAL_FOOD::add)));
	}

	public static ItemEntry<BlazingFoodItem> apple(String baseName, UnaryOperator<BlazingFood.Builder> builder) {
		return BlazingFood.create(baseName + "_apple",
				b -> builder.apply(b
						.tags(BlazingTags.Items.METAL_APPLES.tag())
						.nutrition(4)
						.saturation(1.1f)
						.onRegisterAfter(BlazingItems.METAL_FOOD::add)));
	}

	public static ItemEntry<BlazingFoodItem> stellarApple(String baseName, UnaryOperator<BlazingFood.Builder> builder) {
		return BlazingFood.create("stellar_" + baseName + "_apple",
				b -> builder.apply(b
						.rarity(Rarity.RARE)
						.tags(BlazingTags.Items.STELLAR_METAL_APPLES.tag())
						.nutrition(4)
						.saturation(1.1f)
						.onRegisterAfter(BlazingItems.METAL_FOOD::add)));
	}

	public static ItemEntry<BlazingFoodItem> enchantedApple(String baseName, UnaryOperator<BlazingFood.Builder> builder) {
		return BlazingFood.create("enchanted_" + baseName + "_apple",
				b -> builder.apply(b
						.rarity(Rarity.EPIC)
						.foil()
						.tags(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag())
						.nutrition(4)
						.saturation(1.1f)
						.onRegisterAfter(BlazingItems.METAL_FOOD::add)));
	}

}
