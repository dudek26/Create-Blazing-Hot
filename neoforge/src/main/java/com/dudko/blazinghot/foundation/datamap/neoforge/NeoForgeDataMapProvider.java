package com.dudko.blazinghot.foundation.datamap.neoforge;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelDataEntry;
import com.dudko.blazinghot.registry.neoforge.BlazingDataMapsNeoForge;
import com.dudko.blazinghot.registry.neoforge.BlazingFluidsImpl;
import com.simibubi.create.AllFluids;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluids;

import net.neoforged.neoforge.common.data.DataMapProvider;

public class NeoForgeDataMapProvider extends DataMapProvider {

	public NeoForgeDataMapProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(Provider provider) {
		builder(BlazingDataMapsNeoForge.BLAZE_MIXER_FUEL)
			.add(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.LAVA),
				new BlazeMixerFuelDataEntry(2, 1),
				false)
			.add(BuiltInRegistries.FLUID.wrapAsHolder(BlazingFluidsImpl.NETHER_LAVA.getSource()),
				new BlazeMixerFuelDataEntry(2.5f, 0.5f),
				false)
			.add(BuiltInRegistries.FLUID.wrapAsHolder(AllFluids.HONEY.getSource()),
				new BlazeMixerFuelDataEntry(1.25f, 0.75f),
				false);
	}
}
