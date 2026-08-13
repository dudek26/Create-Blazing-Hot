package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.List;
import java.util.Map;

import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelDataEntry;
import com.mojang.datafixers.util.Either;

import com.mojang.serialization.Codec;

import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public record RecipeMixerFuelOverrides(boolean replace,
									   Map<Either<TagKey<Fluid>, ResourceKey<Fluid>>, BlazeMixerFuelDataEntry> values,
									   List<Either<TagKey<Fluid>, ResourceKey<Fluid>>> remove) {

	public static final RecipeMixerFuelOverrides
		EMPTY =
		new RecipeMixerFuelOverrides(false,
			Map.of(),
			List.of());

	private static final Codec<Either<TagKey<Fluid>, ResourceKey<Fluid>>>
		KEY_CODEC =
		Codec.either(TagKey.codec(Registries.FLUID),
			ResourceKey.codec(Registries.FLUID));

	public static final Codec<RecipeMixerFuelOverrides>
		CODEC =
		RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("replace",
						false)
					.forGetter(RecipeMixerFuelOverrides::replace),
				Codec.unboundedMap(KEY_CODEC,
						BlazeMixerFuelDataEntry.CODEC)
					.fieldOf("values")
					.forGetter(RecipeMixerFuelOverrides::values),
				KEY_CODEC.listOf()
					.optionalFieldOf("remove",
						List.of())
					.forGetter(RecipeMixerFuelOverrides::remove))
			.apply(instance,
				RecipeMixerFuelOverrides::new));


}
