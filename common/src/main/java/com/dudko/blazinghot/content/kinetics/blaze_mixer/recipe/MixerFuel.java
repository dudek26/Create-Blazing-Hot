package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelDataEntry;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public record MixerFuel(long amount, RecipeMixerFuelOverrides overrides) {

	public static final Codec<MixerFuel>
		CODEC =
		RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.fieldOf("amount")
					.forGetter(MixerFuel::amount),
				RecipeMixerFuelOverrides.CODEC.optionalFieldOf("overrides",
						RecipeMixerFuelOverrides.EMPTY)
					.forGetter(MixerFuel::overrides))
			.apply(instance,
				MixerFuel::new));

	public static Builder builder() {
		return new Builder();
	}

	public static MixerFuel simple(long amount) {
		return builder().amount(amount).build();
	}

	public static class Builder {
		private long amount = MultiAmount.fromBucketFraction(1, 20).get();
		private boolean overridesReplace = false;
		private final Map<Either<TagKey<Fluid>, ResourceKey<Fluid>>, BlazeMixerFuelDataEntry> overridesValues = new HashMap<>();
		private final List<Either<TagKey<Fluid>, ResourceKey<Fluid>>> overridesRemove = new ArrayList<>();

		public Builder amount(long amount) {
			this.amount = amount;
			return this;
		}

		public Builder overridesReplace() {
			this.overridesReplace = true;
			return this;
		}

		public Builder addOverride(Either<TagKey<Fluid>, ResourceKey<Fluid>> key, float speed, float usage) {
			overridesValues.put(key, new BlazeMixerFuelDataEntry(speed, usage));
			return this;
		}

		public Builder addOverride(TagKey<Fluid> tag, float speed, float usage) {
			return addOverride(Either.left(tag), speed, usage);
		}

		public Builder addOverride(ResourceKey<Fluid> resource, float speed, float usage) {
			return addOverride(Either.right(resource), speed, usage);
		}

		public Builder removeOverride(Either<TagKey<Fluid>, ResourceKey<Fluid>> key) {
			overridesRemove.add(key);
			return this;
		}

		public Builder removeOverride(TagKey<Fluid> tag) {
			return removeOverride(Either.left(tag));
		}

		public Builder removeOverride(ResourceKey<Fluid> resource) {
			return removeOverride(Either.right(resource));
		}

		public MixerFuel build() {
			RecipeMixerFuelOverrides overrides = new RecipeMixerFuelOverrides(overridesReplace, overridesValues, overridesRemove);
			return new MixerFuel(amount, overrides);
		}

	}


}
