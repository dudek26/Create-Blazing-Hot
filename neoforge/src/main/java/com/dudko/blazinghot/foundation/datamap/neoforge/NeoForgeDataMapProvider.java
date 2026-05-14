package com.dudko.blazinghot.foundation.datamap.neoforge;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelBuilder;
import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelData;
import com.dudko.blazinghot.registry.neoforge.BlazingDataMapsNeoForge;
import com.dudko.blazinghot.registry.neoforge.BlazingFluidsImpl;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import net.neoforged.neoforge.common.data.DataMapProvider;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeoForgeDataMapProvider extends DataMapProvider {

	public NeoForgeDataMapProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(Provider provider) {
		mixerFuel(Fluids.LAVA,
			b -> b
				.speed(2)
				.override(BlazeMixerBlockEntity.MixingType.BLAZE_MIXING, 1, 1));
		mixerFuel(BlazingFluidsImpl.NETHER_LAVA.getSource(),
			b -> b
				.speed(2.5f)
				.usage(0.5f)
				.override(BlazeMixerBlockEntity.MixingType.BLAZE_MIXING, 1.25f, 0.5f));
	}

	private void mixerFuel(Fluid fluid, UnaryOperator<BlazeMixerFuelBuilder> builder) {
		builder(BlazingDataMapsNeoForge.BLAZE_MIXER_FUEL)
			.add(BuiltInRegistries.FLUID.wrapAsHolder(fluid),
				builder.apply(BlazeMixerFuelData.builder()).build(),
				false);
	}
}
