package com.dudko.blazinghot.foundation.mixin_interfaces;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.MixerFuel;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface IProcessingRecipeParams {

	static <P extends ProcessingRecipeParams> MapCodec<P> codec(MapCodec<P> original) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
				original.forGetter(Function.identity()),
				MixerFuel.CODEC.optionalFieldOf("blazinghot:fuel")
					.forGetter(params -> cast(params).blazinghot$getMixerFuel()))
			.apply(instance,
				(params, fuel) -> {
					cast(params).blazinghot$setMixerFuel(fuel);
					return params;
				}));
	}

	Optional<MixerFuel> blazinghot$getMixerFuel();

	void blazinghot$setMixerFuel(Optional<MixerFuel> fuel);

	default void blazinghot$setMixerFuel(@Nullable MixerFuel fuel) {
		blazinghot$setMixerFuel(Optional.ofNullable(fuel));
	}

	static IProcessingRecipeParams cast(ProcessingRecipeParams params) {
		return (IProcessingRecipeParams) params;
	}

}
