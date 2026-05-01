package com.dudko.blazinghot.foundation.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BlazeMixerFuelData(float speed, float usage) {
	public static final Codec<BlazeMixerFuelData> CODEC =
		RecordCodecBuilder.create(instance -> instance.group(
				Codec.floatRange(0, 100).fieldOf("speed").forGetter(BlazeMixerFuelData::speed),
				Codec.floatRange(0, 100).fieldOf("usage").forGetter(BlazeMixerFuelData::usage))
			.apply(instance, BlazeMixerFuelData::new));
}
