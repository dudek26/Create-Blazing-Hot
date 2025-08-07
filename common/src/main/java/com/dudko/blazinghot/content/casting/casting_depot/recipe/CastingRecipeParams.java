package com.dudko.blazinghot.content.casting.casting_depot.recipe;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class CastingRecipeParams extends ProcessingRecipeParams {

	public static MapCodec<CastingRecipeParams>
			CODEC =
			RecordCodecBuilder.mapCodec(instance -> instance
					.group(codec(CastingRecipeParams::new).forGetter(Function.identity()),
							Codec.BOOL.optionalFieldOf("keep_mold", false).forGetter(CastingRecipeParams::keepMold),
							Codec.INT
									.optionalFieldOf("cooling_duration", 0)
									.forGetter(CastingRecipeParams::getCoolingDuration))
					.apply(instance, (params, keepMold, coolingDuration) -> {
						params.keepMold = keepMold;
						params.coolingDuration = coolingDuration;
						return params;
					}));
	public static StreamCodec<RegistryFriendlyByteBuf, CastingRecipeParams>
			STREAM_CODEC =
			streamCodec(CastingRecipeParams::new);

	protected boolean keepMold;
	protected int coolingDuration;

	protected CastingRecipeParams() {
		keepMold = false;
		coolingDuration = 0;
	}

	public boolean keepMold() {
		return keepMold;
	}

	public int getCoolingDuration() {
		return coolingDuration;
	}

	@Override
	protected void encode(RegistryFriendlyByteBuf buffer) {
		super.encode(buffer);
		ByteBufCodecs.BOOL.encode(buffer, keepMold);
		ByteBufCodecs.VAR_INT.encode(buffer, coolingDuration);
	}

	@Override
	protected void decode(RegistryFriendlyByteBuf buffer) {
		super.decode(buffer);
		keepMold = ByteBufCodecs.BOOL.decode(buffer);
		coolingDuration = ByteBufCodecs.VAR_INT.decode(buffer);
	}
}
