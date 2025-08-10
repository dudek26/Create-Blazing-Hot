package com.dudko.blazinghot.content.item.food;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BlazingFood {

	public final String name;
	public final BlazingMetal metal;
	public final FoodProperties foodProperties;
	public final Item.Properties itemProperties;
	public final List<BlazingFoodProperty<?>> extraProperties;
	public final List<BlazingFoodVariant> variants;

	protected BlazingFood(Builder builder) {
		this.name = builder.name;
		this.metal = builder.metal;
		this.foodProperties = builder.foodProperties.build();
		this.itemProperties = builder.itemProperties;
		this.extraProperties = List.copyOf(builder.extraProperties);
		this.variants = List.copyOf(builder.variants);
	}

	public static BlazingFood create(String name, BlazingMetal metal, UnaryOperator<Builder> transform) {
		Builder builder = new Builder(name, metal);
		transform.apply(builder);
		return builder.build();
	}

	public ItemEntry<BlazingFoodItem> register() {
		variants.forEach(variant -> BlazingHot.registrate().item(metal.name + ));
	}

	public static class Builder {

		private final String name;
		private final BlazingMetal metal;
		private final FoodProperties.Builder foodProperties;
		private final Item.Properties itemProperties;
		private final List<BlazingFoodProperty<?>> extraProperties;
		private final List<BlazingFoodVariant> variants;

		private Builder(String name, BlazingMetal metal) {
			this.name = name;
			this.metal = metal;
			foodProperties = new FoodProperties.Builder();
			itemProperties = new Item.Properties();
			extraProperties = new ArrayList<>();
			variants = new ArrayList<>();
		}

		public Builder addVariant(String name, UnaryOperator<BlazingFoodVariant.Builder> transform) {
			BlazingFoodVariant.Builder variantBuilder = BlazingFoodVariant.builder(name);
			transform.apply(variantBuilder);
			variants.add(variantBuilder.build());
			return this;
		}

		public BlazingFood build() {
			return new BlazingFood(this);
		}

	}

}
