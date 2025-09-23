package com.dudko.blazinghot.content.item.food;

import static com.dudko.blazinghot.data.lang.ItemDescriptions.slownessRemovedDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BlazingFood {

	public final String name;
	public final Item.Properties itemProperties;
	public final BiConsumer<Level, LivingEntity> onUse;
	public final boolean generateRecipe;
	public final List<NonNullConsumer<? super BlazingFoodItem>> onRegisterAfter;
	public final List<TagKey<Item>> tags;

	protected BlazingFood(Builder builder) {
		this.name = builder.name;
		this.itemProperties = builder.itemProperties;
		this.generateRecipe = builder.generateRecipe;
		this.onUse = builder.onUse;
		this.onRegisterAfter = builder.onRegisterAfter;
		this.tags = new ArrayList<>();
	}

	public static ItemEntry<BlazingFoodItem> create(String name, UnaryOperator<Builder> builder) {
		return builder.apply(new Builder(name)).register();
	}

	private ItemEntry<BlazingFoodItem> register() {
		ItemBuilder<BlazingFoodItem, ?>
				builder =
				BlazingHot
						.registrate()
						.item(name, properties -> new BlazingFoodItem(properties, onUse))
						.properties(properties -> itemProperties);
		for (NonNullConsumer<? super BlazingFoodItem> consumer : onRegisterAfter) {
			builder.onRegisterAfter(Registries.ITEM, consumer);
		}

		return builder.register();
	}

	public static class Builder {
		private final String name;
		private final FoodProperties.Builder foodProperties;
		private final Item.Properties itemProperties;
		private BiConsumer<Level, LivingEntity> onUse;
		private boolean generateRecipe;
		private final List<NonNullConsumer<? super BlazingFoodItem>> onRegisterAfter;
		private final List<TagKey<Item>> tags;

		private Builder(String name) {
			this.name = name;
			this.onUse = ((level, entity) -> {
			});
			this.foodProperties = new FoodProperties.Builder();
			this.itemProperties = new Item.Properties();
			this.generateRecipe = true;
			this.onRegisterAfter = new ArrayList<>();
			this.tags = new ArrayList<>();
		}

		/**
		 * Defines the callback on item consumption.
		 */
		public Builder onUse(BiConsumer<Level, LivingEntity> onUse) {
			this.onUse = onUse;
			return this;
		}

		/**
		 * Defines food properties.
		 */
		public Builder food(UnaryOperator<FoodProperties.Builder> transform) {
			transform.apply(foodProperties);
			return this;
		}

		/**
		 * Defines item properties. Do not define food properties here, use {@link Builder#food(UnaryOperator)} instead.
		 */
		public Builder properties(UnaryOperator<Item.Properties> transform) {
			transform.apply(itemProperties);
			return this;
		}

		public Builder onRegisterAfter(NonNullConsumer<? super BlazingFoodItem> onRegisterAfter) {
			this.onRegisterAfter.add(onRegisterAfter);
			return this;
		}

		// ITEM PROPERTIES SHORTCUTS

		/**
		 * Makes the item fire-resistant.
		 */
		public Builder fireResistant() {
			return properties(Item.Properties::fireResistant);
		}

		/**
		 * Adds an enchantment glint.
		 */
		public Builder foil() {
			return properties(properties -> properties.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
		}

		/**
		 * Sets the item's rarity.
		 */
		public Builder rarity(Rarity rarity) {
			return properties(properties -> properties.rarity(rarity));
		}

		// FOOD PROPERTIES SHORTCUTS

		public Builder nutrition(int nutrition) {
			return food(foodProperties -> foodProperties.nutrition(nutrition));
		}

		public Builder saturation(float saturationModifier) {
			return food(foodProperties -> foodProperties.saturationModifier(saturationModifier));
		}

		public Builder alwaysEat() {
			return food(FoodProperties.Builder::alwaysEdible);
		}

		public Builder effect(Holder<MobEffect> effect, int duration, int amplifier, float probability) {
			return food(foodProperties -> foodProperties.effect(new MobEffectInstance(effect, duration, amplifier),
					probability));
		}

		public Builder effect(Holder<MobEffect> effect, int duration, int amplifier) {
			return effect(effect, duration, amplifier, 1);
		}

		public Builder effect(Holder<MobEffect> effect, int duration) {
			return effect(effect, duration, 0);
		}

		// ON USE SHORTCUTS

		/**
		 * Extinguishes player's fire.
		 */
		public Builder extinguish() {
			onRegisterAfter(c -> ItemDescription.useKey(c, ItemDescriptions.EXTINGUISHING_FOOD.getKey()));
			return onUse((level, entity) -> {
				if (entity instanceof Player player
						&& !level.isClientSide()
						&& entity.getRemainingFireTicks() > 0
						&& entity.getHealth() < 4) {
					BlazingAdvancements.EXTINGUISHING_FOOD_SAVE.awardTo(player);
				}
				entity.extinguishFire();
			});
		}

		/**
		 * Restores player's oxygen.
		 */
		public Builder addOxygen(int oxygen) {
			onRegisterAfter(c -> ItemDescription.useKey(c, ItemDescriptions.OXYGEN_FOOD.getKey()));
			return onUse((level, entity) -> entity.setAirSupply(Math.min(entity.getAirSupply() + oxygen,
					entity.getMaxAirSupply())));
		}

		/**
		 * Removes slowness up to the specified amplifier. Does nothing if effect's amplifier is higher than max specified.
		 */
		public Builder removeSlowness(int maxAmplifier) {
			onRegisterAfter(c -> ItemDescription.useKey(c, slownessRemovedDescription(maxAmplifier).getKey()));
			return onUse((level, entity) -> {
				if (maxAmplifier >= 0
						&& entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)
						&& Objects.requireNonNull(entity.getEffect(MobEffects.MOVEMENT_SLOWDOWN)).getAmplifier()
						<= maxAmplifier) {
					entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
				}
			});
		}

		/**
		 * Removes slowness, no matter the amplifier.
		 */
		public Builder removeSlowness() {
			onRegisterAfter(c -> ItemDescription.useKey(c, slownessRemovedDescription(255).getKey()));
			return onUse((level, entity) -> {
				if (entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
					entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
				}
			});
		}

		@SafeVarargs
		public final Builder tags(TagKey<Item>... tags) {
			this.tags.addAll(Arrays.asList(tags));
			return this;
		}

		public Builder transform(UnaryOperator<Builder> transform) {
			transform.apply(this);
			return this;
		}

		public BlazingFood build() {
			itemProperties.food(foodProperties.build());
			return new BlazingFood(this);
		}

		public ItemEntry<BlazingFoodItem> register() {
			return build().register();
		}

	}

}
