package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

// useless for now
public class BlazeMixingRecipeParams extends ProcessingRecipeParams {

	public static MapCodec<BlazeMixingRecipeParams>
			CODEC =
			RecordCodecBuilder.mapCodec(instance -> instance
					.group(codec(BlazeMixingRecipeParams::new).forGetter(Function.identity()),
							FluidIngredient.CODEC
									.optionalFieldOf("mixer_fuel")
									.forGetter(BlazeMixingRecipeParams::getMixerFuel))
					.apply(instance, (params, mixerFuel) -> {
						params.mixerFuel = mixerFuel.orElse(null);
						return params;
					}));
	public static StreamCodec<RegistryFriendlyByteBuf, BlazeMixingRecipeParams>
			STREAM_CODEC =
			streamCodec(BlazeMixingRecipeParams::new);

	protected FluidIngredient mixerFuel = null;

	public BlazeMixingRecipeParams() {
		super();
	}

	protected final Optional<FluidIngredient> getMixerFuel() {
		return Optional.ofNullable(mixerFuel);
	}

	@Override
	protected void encode(RegistryFriendlyByteBuf buffer) {
		super.encode(buffer);
//		FluidIngredient.write(buffer, mixerFuel);
	}

	@Override
	protected void decode(RegistryFriendlyByteBuf buffer) {
		super.decode(buffer);
//		mixerFuel = FluidIngredient.read(buffer);
	}

}
