package com.dudko.blazinghot.content.item.food;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BlazingFoodVariant {

	public final UnaryOperator<String> name;
	public final Item.Properties itemProperties;
	public final BiConsumer<Level, LivingEntity> onUse;
	public final boolean generateRecipe;
	public final @Nullable Consumer<? super BlazingFoodItem> onRegisterAfter;

	protected BlazingFoodVariant(Builder builder) {
		this.name = builder.name;
		this.itemProperties = builder.itemProperties;
		this.generateRecipe = builder.generateRecipe;
		this.onUse = builder.onUse;
		this.onRegisterAfter = builder.onRegisterAfter;
	}

	public static Builder builder(String name) {
		return new Builder(name);
	}

	public static class Builder {
		private UnaryOperator<String> name;
		private final FoodProperties.Builder foodProperties;
		private final Item.Properties itemProperties;
		private BiConsumer<Level, LivingEntity> onUse;
		private boolean generateRecipe;
		private @Nullable Consumer<? super BlazingFoodItem> onRegisterAfter;

		private Builder(String name) {
			this.name = foodName -> foodName + "_" + name;
			this.onUse = ((level, entity) -> {
			});
			this.foodProperties = new FoodProperties.Builder();
			this.itemProperties = new Item.Properties();
			this.generateRecipe = true;
			this.onRegisterAfter = null;
		}

		/**
		 * Defines a function returning the full variant name.
		 */
		public Builder name(UnaryOperator<String> name) {
			this.name = name;
			return this;
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
			this.onRegisterAfter = onRegisterAfter;
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

		// ON USE SHORTCUTS

		/**
		 * Extinguishes player's fire.
		 */
		public Builder extinguish() {
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
			return onUse((level, entity) -> entity.setAirSupply(Math.min(entity.getAirSupply() + oxygen,
					entity.getMaxAirSupply())));
		}

		/**
		 * Removes slowness up to the specified amplifier. Does nothing if effect's amplifier is higher than max specified.
		 */
		public Builder removeSlowness(int maxAmplifier) {
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
			return onUse((level, entity) -> {
				if (entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
					entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
				}
			});
		}

		public Builder transform(UnaryOperator<Builder> transform) {
			transform.apply(this);
			return this;
		}

		public BlazingFoodVariant build() {
			itemProperties.food(foodProperties.build());
			return new BlazingFoodVariant(this);
		}

	}

}
