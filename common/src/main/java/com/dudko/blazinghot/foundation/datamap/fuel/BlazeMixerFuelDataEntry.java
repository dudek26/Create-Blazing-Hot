package com.dudko.blazinghot.foundation.datamap.fuel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BlazeMixerFuelDataEntry(float speed, float usage) {

	public long calculateFuelUsage(long amount) {
		return (long) (usage * amount);
	}

	public static final Codec<BlazeMixerFuelDataEntry> CODEC =
		RecordCodecBuilder.create(instance -> instance.group(
				Codec.floatRange(0, 100).fieldOf("speed").forGetter(BlazeMixerFuelDataEntry::speed),
				Codec.floatRange(0, 100).fieldOf("usage").forGetter(BlazeMixerFuelDataEntry::usage))
			.apply(instance, BlazeMixerFuelDataEntry::new));
}
