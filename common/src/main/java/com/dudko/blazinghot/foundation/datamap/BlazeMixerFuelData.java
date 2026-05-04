package com.dudko.blazinghot.foundation.datamap;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;

public record BlazeMixerFuelData(float speed, float usage) {

	private static final Map<Holder<Fluid>, BlazeMixerFuelData> FUELS = new HashMap<>();
	public static boolean updated = false;

	public static Map<Holder<Fluid>, BlazeMixerFuelData> getFuels() {
		if (!updated) {
			updated = true;
			updateFuels();
		}
		return FUELS;
	}

	public static BlazeMixerFuelData getFuelData(Holder<Fluid> fluid) {
		return FUELS.get(fluid);
	}

	public static BlazeMixerFuelData getFuelData(Fluid fluid) {
		return FUELS.get(BuiltInRegistries.FLUID.wrapAsHolder(fluid));
	}

	public long calculateFuelUsage(long amount) {
		return (long) (usage * amount);
	}

	private static void updateFuels() {
		FUELS.clear();
		BuiltInRegistries.FLUID.holders().forEach(fluid -> {
			BlazeMixerFuelData data = getData(fluid);
			if (data == null) return;
			FUELS.put(fluid, data);
		});
	}

	@ExpectPlatform
	public static BlazeMixerFuelData getData(Holder<Fluid> fluid) {
		throw new AssertionError();
	}

	public static final Codec<BlazeMixerFuelData> CODEC =
		RecordCodecBuilder.create(instance -> instance.group(
				Codec.floatRange(0, 100).fieldOf("speed").forGetter(BlazeMixerFuelData::speed),
				Codec.floatRange(0, 100).fieldOf("usage").forGetter(BlazeMixerFuelData::usage))
			.apply(instance, BlazeMixerFuelData::new));
}
