package com.dudko.blazinghot.foundation.datamap.fuel;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity.MixingType;
import com.dudko.blazinghot.foundation.codec.BlazingCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;

public record BlazeMixerFuelData(BlazeMixerFuelDataEntry base,
								 Map<MixingType, BlazeMixerFuelDataEntry> overrides) {

	public static final Codec<BlazeMixerFuelData> CODEC =
		RecordCodecBuilder.create(instance -> instance.group(
			Codec.floatRange(0, 100)
				.fieldOf("speed")
				.forGetter(data -> data.base.speed()),
			Codec.floatRange(0, 100)
				.fieldOf("usage")
				.forGetter(data -> data.base.usage()),
			Codec.unboundedMap(BlazingCodecs.stringEnum(MixingType.class), BlazeMixerFuelDataEntry.CODEC)
				.fieldOf("overrides")
				.forGetter(BlazeMixerFuelData::overrides)
		).apply(instance, (speed, usage, overrides) -> new BlazeMixerFuelData(new BlazeMixerFuelDataEntry(speed, usage), overrides)));

	public static BlazeMixerFuelBuilder builder() {
		return new BlazeMixerFuelBuilder();
	}

	private static final Map<Holder<Fluid>, BlazeMixerFuelData> FUELS = new HashMap<>();
	public static boolean updated = false;

	public static Map<Holder<Fluid>, BlazeMixerFuelData> getFuels() {
		if (!updated) {
			updateFuels();
		}
		return FUELS;
	}

	private static void updateFuels() {
		FUELS.clear();
		BuiltInRegistries.FLUID.holders().forEach(fluid -> {
			BlazeMixerFuelData data = getData(fluid);
			if (data == null) return;
			FUELS.put(fluid, data);
		});
		updated = true;
	}

	@ExpectPlatform
	public static BlazeMixerFuelData getData(Holder<Fluid> fluid) {
		throw new AssertionError();
	}

	public static BlazeMixerFuelData getFuelData(Holder<Fluid> fluid) {
		return FUELS.get(fluid);
	}

	@Nullable
	public static BlazeMixerFuelData getFuelData(Fluid fluid) {
		return getFuels().get(BuiltInRegistries.FLUID.wrapAsHolder(fluid));
	}

	@NotNull
	public BlazeMixerFuelDataEntry getForType(MixingType type) {
		BlazeMixerFuelDataEntry override = overrides.get(type);
		if (override != null) return override;

		return base;
	}

	public long calculateFuelUsage(MixingType type, long amount) {
		return getForType(type).calculateFuelUsage(amount);
	}

	public float getSpeed(MixingType type) {
		return getForType(type).speed();
	}

}
