package com.dudko.blazinghot.content.item.food;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import net.minecraft.core.registries.Registries;

public class BlazingFood {

	public final String name;
	public final BlazingMetal metal;
	public final List<BlazingFoodVariant> variants;

	protected BlazingFood(Builder builder) {
		this.name = builder.name;
		this.metal = builder.metal;
		this.variants = List.copyOf(builder.variants);
	}

	public static BlazingFood create(String name, BlazingMetal metal, UnaryOperator<Builder> transform) {
		Builder builder = new Builder(name, metal);
		transform.apply(builder);
		return builder.build();
	}

	public static BlazingFood create(BlazingMetal metal, UnaryOperator<Builder> transform) {
		return create(metal.name, metal, transform);
	}

	public List<ItemEntry<BlazingFoodItem>> register(CreateRegistrate registrate) {
		List<ItemEntry<BlazingFoodItem>> items = new ArrayList<>();

		variants.forEach(variant -> {
			ItemBuilder<BlazingFoodItem, ?>
					builder =
					registrate
							.item(variant.name.apply(name),
									properties -> new BlazingFoodItem(properties, variant.onUse))
							.properties(properties -> variant.itemProperties);
			if (variant.onRegisterAfter instanceof NonNullConsumer<? super BlazingFoodItem> consumer) {
				builder.onRegisterAfter(Registries.ITEM, consumer);
			}
			items.add(builder.register());
		});

		return items;
	}

	public static class Builder {

		private final String name;
		private final BlazingMetal metal;
		private final List<BlazingFoodVariant> variants;

		private Builder(String name, BlazingMetal metal) {
			this.name = name;
			this.metal = metal;
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
