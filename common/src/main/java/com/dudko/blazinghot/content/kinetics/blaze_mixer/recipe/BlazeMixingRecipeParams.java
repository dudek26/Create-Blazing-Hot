package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

// useless for now
public class BlazeMixingRecipeParams extends ProcessingRecipeParams {

	public static MapCodec<BlazeMixingRecipeParams>
			CODEC =
			RecordCodecBuilder.mapCodec(instance -> instance
					.group(codec(BlazeMixingRecipeParams::new).forGetter(Function.identity()),
							SizedFluidIngredient.FLAT_CODEC
									.optionalFieldOf("mixer_fuel")
									.forGetter(BlazeMixingRecipeParams::getMixerFuel))
					.apply(instance, (params, mixerFuel) -> {
						params.mixerFuel = mixerFuel.orElse(null);
						return params;
					}));
	public static StreamCodec<RegistryFriendlyByteBuf, BlazeMixingRecipeParams>
			STREAM_CODEC =
			streamCodec(BlazeMixingRecipeParams::new);

	protected SizedFluidIngredient mixerFuel = null;

	public BlazeMixingRecipeParams() {
		super();
	}

	protected final Optional<SizedFluidIngredient> getMixerFuel() {
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
