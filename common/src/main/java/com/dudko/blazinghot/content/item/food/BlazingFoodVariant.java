package com.dudko.blazinghot.content.item.food;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BlazingFoodVariant {

	public final String name;
	public final Supplier<@Nullable Item> parent;
	public final FoodProperties foodProperties;
	public final Item.Properties itemProperties;
	public final List<BlazingFoodProperty<?>> extraProperties;
	public final boolean generateRecipe;

	protected BlazingFoodVariant(Builder builder) {
		this.name = builder.name;
		this.parent = builder.parent;
		this.foodProperties = builder.foodProperties.build();
		this.itemProperties = builder.itemProperties;
		this.extraProperties = List.copyOf(builder.extraProperties);
		this.generateRecipe = builder.generateRecipe;
	}

	public static Builder builder(String name) {
		return new Builder(name);
	}

	public static class Builder {
		private final String name;
		private Supplier<@Nullable Item> parent;
		private final FoodProperties.Builder foodProperties;
		private final Item.Properties itemProperties;
		private final List<BlazingFoodProperty<?>> extraProperties;
		private boolean generateRecipe;

		private Builder(String name) {
			this.name = name;
			this.parent = () -> null;
			this.foodProperties = new FoodProperties.Builder();
			this.itemProperties = new Item.Properties();
			this.extraProperties = new ArrayList<>();
			this.generateRecipe = true;
		}

		public BlazingFoodVariant build() {
			return new BlazingFoodVariant(this);
		}

	}

}
