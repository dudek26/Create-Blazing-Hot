package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class BlazingPartialModels {

	public static final PartialModel BLAZE_MIXER_HEAD = block("blaze_mixer/head");
	public static final PartialModel BLAZE_MIXER_HEAD_BLAZE = block("blaze_mixer/head_blaze");
	public static final PartialModel BLAZE_MIXER_POLE = block("blaze_mixer/pole");
	public static final PartialModel SHAFTLESS_CRIMSON_COGWHEEL = block("crimson_cogwheel_shaftless");

	private static PartialModel block(String path) {
		return PartialModel.of(BlazingHot.asResource("block/" + path));
	}

	public static void init() {
	}

}
