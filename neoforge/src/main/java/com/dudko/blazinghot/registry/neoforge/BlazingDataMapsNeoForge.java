package com.dudko.blazinghot.registry.neoforge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelData;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class BlazingDataMapsNeoForge {

	public static void register(RegisterDataMapTypesEvent event) {
		event.register(BlazingDataMapsNeoForge.BLAZE_MIXER_FUEL);
	}

	public static final DataMapType<Fluid, BlazeMixerFuelData> BLAZE_MIXER_FUEL =
		DataMapType.builder(
				BlazingHot.asResource("blaze_mixer_fuel"),
				Registries.FLUID,
				BlazeMixerFuelData.CODEC
			).synced(BlazeMixerFuelData.CODEC, true)
			.build();

}
