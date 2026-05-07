package com.dudko.blazinghot.foundation.datamap.fuel.neoforge;

import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelDataEntry;
import com.dudko.blazinghot.registry.neoforge.BlazingDataMapsNeoForge;

import net.minecraft.core.Holder;
import net.minecraft.world.level.material.Fluid;

public class BlazeMixerFuelDataImpl {

	public static BlazeMixerFuelDataEntry getData(Holder<Fluid> fluid) {
		return fluid.getData(BlazingDataMapsNeoForge.BLAZE_MIXER_FUEL);
	}

}
