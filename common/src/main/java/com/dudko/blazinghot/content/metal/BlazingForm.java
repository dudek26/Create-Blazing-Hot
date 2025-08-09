package com.dudko.blazinghot.content.metal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingTags;

import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class BlazingForm {

	public final String name;
	public final String tagFolder;
	public final List<Mods> mods;
	public final @Nullable ResourceLocation customLocation;
	public final @Nullable Molds.Mold mold;

	public final MultiAmount amount;
	public final int meltingTime;
	public final int castingTime;
	public final int coolingTime;
	public final MultiAmount fuelCost;

	public final boolean mechanicalMixerMeltable;
	public final boolean optional;

	public final List<Flag> flags;

	private BlazingForm(Builder builder) {
		name = builder.name;
		tagFolder = builder.tagFolder;
		mods = List.copyOf(builder.mods);
		customLocation = builder.customLocation;
		mold = builder.mold;

		assert builder.amount != null;
		amount = builder.amount;

		meltingTime = builder.meltingTime;
		castingTime = builder.castingTime;
		coolingTime = builder.coolingTime;

		assert builder.fuelCost != null;
		fuelCost = builder.fuelCost;

		mechanicalMixerMeltable = builder.mechanicalMixerMeltable;
		optional = builder.optional;
		flags = List.copyOf(builder.flags);
	}

	/**
	 * Creates a new BlazingForm.
	 *
	 * @param name            Name of the form, used to automatically generate casting recipes
	 * @param builderFunction Builder function
	 */
	public static BlazingForm create(String name, UnaryOperator<Builder> builderFunction) {
		return builderFunction.apply(new Builder(name)).build();
	}

	/**
	 * Creates a new BlazingForm from an existing one.
	 *
	 * @param name            Name of the new form
	 * @param builderFunction Builder function
	 * @return New BlazingForm
	 */
	public BlazingForm createFrom(String name, UnaryOperator<Builder> builderFunction) {
		Builder builder = new Builder(name);
		builder.tagFolder = tagFolder;
		builder.mods = new ArrayList<>(mods);
		builder.customLocation = customLocation;

		builder.mold = mold;
		builder.amount = amount;

		builder.meltingTime = meltingTime;
		builder.castingTime = castingTime;
		builder.coolingTime = coolingTime;

		builder.fuelCost = fuelCost;

		builder.mechanicalMixerMeltable = mechanicalMixerMeltable;
		builder.optional = optional;

		builder.flags = new ArrayList<>(flags);

		return builderFunction.apply(builder).build();
	}

	public Ingredient getMeltingIngredient(BlazingMetal metal) {
		if (customLocation != null) return Ingredient.of(BuiltInRegistries.ITEM.get(customLocation));
		return Ingredient.of(getItemTag(metal));
	}

	public List<LoadCondition<?>> getMeltingLoadConditions(BlazingMetal metal) {
		List<LoadCondition<?>> conditions = new ArrayList<>();
		if (customLocation != null) {
			List<Mods> formMods = this.mods.stream().filter(f -> !f.alwaysIncluded).toList();
			List<Mods> metalMods = metal.mods.stream().filter(m -> !m.alwaysIncluded).toList();

			if (!metalMods.isEmpty()) conditions.add(DefaultLoadConditions.anyModLoaded(metalMods));
			if (!formMods.isEmpty()) conditions.add(DefaultLoadConditions.anyModLoaded(formMods));
			return conditions;
		}
		else if (optional) {
			conditions.add(DefaultLoadConditions.tagsPopulated(getItemTag(metal)));
		}

		return conditions;
	}

	public String getMeltingRecipeName(BlazingMetal metal) {
		if (customLocation != null) return "melting/" + customLocation.getPath();
		return "melting/" + tagFolder + "/" + metal.name;
	}

	public ResourceLocation getCastingResult(BlazingMetal metal, Mods mod) {
		if (customLocation != null) return customLocation;
		return mod.asResource(metal.name + "_" + name);
	}

	public String getCastingRecipeName(Molds.MoldType moldType, BlazingMetal metal) {
		if (customLocation != null) return moldType + "/" + customLocation.getPath();
		return moldType + "/" + name + "/" + metal.name;
	}

	public TagKey<Item> getItemTag(BlazingMetal metal) {
		return getItemTag(metal.name);
	}

	public TagKey<Item> getItemTag(String metal) {
		return BlazingTags.itemTag(BlazingTags.Namespace.COMMON.asResource(tagFolder + "/" + metal));
	}

	public List<Mods> getMods(BlazingMetal metal) {
		if (mods.isEmpty()) return metal.mods;
		return mods;
	}

	// Shortcuts

	/**
	 * Creates a new BlazingForm from an existing one and makes it optional or not.
	 */
	public BlazingForm asOptional(boolean optional) {
		return createFrom(name, b -> b.optional(optional));
	}

	/**
	 * Creates a new BlazingForm from an existing one with specified mods.
	 */
	public BlazingForm fromMods(Mods... mods) {
		return createFrom(name, b -> b.clearMods().fromMods(mods));
	}

	/**
	 * Creates a new BlazingForm form an existing one with specified datagen flags. All flags will be applied at default.
	 */
	public BlazingForm withFlags(Flag... flags) {
		return createFrom(name, b -> b.setFlags(flags));
	}


	public static class Builder {

		private final String name;
		private String tagFolder;
		private List<Mods> mods;
		private @Nullable ResourceLocation customLocation;
		private @Nullable Molds.Mold mold;

		private @Nullable MultiAmount amount;
		private int meltingTime;
		private int castingTime;
		private int coolingTime;
		private @Nullable MultiAmount fuelCost;

		private boolean mechanicalMixerMeltable;
		private boolean optional;

		private List<Flag> flags;

		private Builder(String name) {
			this.name = name;
			this.tagFolder = "";
			this.mods = new ArrayList<>();

			this.amount = null;
			this.meltingTime = -1;
			this.castingTime = -1;
			this.coolingTime = -1;
			this.fuelCost = null;

			this.mechanicalMixerMeltable = false;
			this.optional = false;

			this.flags = new ArrayList<>();
			flags.add(Flag.MELTING);
			flags.add(Flag.CASTING);
		}

		/**
		 * Defines the tag folder used in melting recipes.
		 *
		 * @param tagFolder Tag folder, should be plural.
		 */
		public Builder withTagFolder(String tagFolder) {
			this.tagFolder = tagFolder;
			return this;
		}

		/**
		 * Defines which mods add this form. Recipes with this form will be loaded if any mod in the list is present.
		 *
		 * @param mods Mods adding this form. When multiple mods are present, casting recipes will be loaded for the first present mod.
		 */
		public Builder fromMods(Mods... mods) {
			this.mods.addAll(List.of(mods));
			return this;
		}

		/**
		 * Clears the list of mods which add this form. Useful for creating forms from other ones.
		 */
		public Builder clearMods() {
			this.mods.clear();
			return this;
		}

		/**
		 * Defines an item that will be used instead of tag for melting and instead of name for casting, for example, Ancient Debris.
		 *
		 * @param location ResourceLocation of the item.
		 */
		public Builder withCustomItem(@Nullable ResourceLocation location) {
			this.customLocation = location;
			return this;
		}

		/**
		 * Shortcut for custom location.
		 *
		 * @see Builder#withCustomItem(ResourceLocation)
		 */
		public Builder withCustomItem(ItemLike item) {
			return withCustomItem(BuiltInRegistries.ITEM.getKey(item.asItem()));
		}

		/**
		 * Defines a mold that will be used in casting recipes. If the mold remains undefined, casting recipes for this form won't be generated.
		 */
		public Builder withMold(@Nullable Molds.Mold mold) {
			this.mold = mold;
			return this;
		}

		/**
		 * Defines the amount of fluid used by the form in casting and created in melting.
		 */
		public Builder withAmount(MultiAmount amount) {
			this.amount = amount;
			return this;
		}

		/**
		 * Defines the time it takes to melt the form in blaze mixing. This value is multiplied in regular mixing.
		 */
		public Builder withMeltingTime(int meltingTime) {
			this.meltingTime = meltingTime;
			return this;
		}

		/**
		 * Overrides the time it takes to fill the casting depot. This value is still multiplied when cooling, unless cooling time is overridden.
		 */
		public Builder overrideCastingTime(int castingTime) {
			this.castingTime = castingTime;
			return this;
		}

		/**
		 * Overrides the time it takes to cool the metal in the casting depot.
		 */
		public Builder overrideCoolingTime(int coolingTime) {
			this.coolingTime = coolingTime;
			return this;
		}

		/**
		 * Overrides the fuel consumption in blaze mixing. If the fuel cost remains undefined, then it's calculated from melting duration.
		 */
		public Builder overrideFuelCost(MultiAmount fuelCost) {
			this.fuelCost = fuelCost;
			return this;
		}

		/**
		 * Makes the form meltable with mechanical mixer.
		 */
		public Builder mechanicalMixerMeltable(boolean value) {
			this.mechanicalMixerMeltable = value;
			return this;
		}

		/**
		 * Makes recipes with this form load only if tag is present.
		 */
		public Builder optional(boolean value) {
			this.optional = value;
			return this;
		}

		/**
		 * Flags define places where this form will be used in datagen.
		 */
		public Builder setFlags(Flag... flags) {
			this.flags.clear();
			this.flags.addAll(List.of(flags));
			return this;
		}

		/**
		 * Builds and returns the form.
		 * <ul>
		 *  If not overridden:
		 * 	<li>casting time is determined from amount</li>
		 * 	<li>cooling time is thrice the value of casting time</li>
		 * 	<li>fuel cost is determined from amount</li>
		 * </ul>
		 *
		 * @throws NullPointerException          if amount is not specified
		 * @throws UnsupportedOperationException if melting time is not specified
		 */
		public BlazingForm build() {
			if (amount == null) throw new NullPointerException("Amount of form " + name + " is null");

			if (meltingTime < 0) throw new UnsupportedOperationException("Melting time of form "
					+ name
					+ " is invalid ("
					+ meltingTime
					+ ")");

			int coolingMultiplier = 3;
			int baseDuration = 50;
			int minDuration = 12;
			int castingDuration = (int) ((float) amount.droplets() / MultiAmount.INGOT.droplets() * baseDuration);

			if (castingTime < 0) {
				castingTime = Math.max(minDuration, castingDuration);
			}
			if (coolingTime < 0) {
				coolingTime = castingTime * coolingMultiplier;
			}
			if (fuelCost == null) {
				fuelCost =
						MultiAmount
								.fromBucketFraction(1, 20)
								.multiply((float) amount.droplets() / MultiAmount.INGOT.droplets());
			}

			return new BlazingForm(this);
		}

	}

	public enum Flag {
		MELTING,
		CASTING
	}
}
