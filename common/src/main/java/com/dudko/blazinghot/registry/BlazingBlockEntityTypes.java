package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerRenderer;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class BlazingBlockEntityTypes {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static final BlockEntityEntry<ModernLampBlockEntity>
			MODERN_LAMP =
			REGISTRATE
					.blockEntity("modern_lamp", ModernLampBlockEntity::new)
					.validBlocks(BlazingBlocks.modernLamps())
					.register();

	public static final BlockEntityEntry<BlazeMixerBlockEntity>
			BLAZE_MIXER =
			REGISTRATE
					.blockEntity("blaze_mixer", BlazeMixerBlockEntity::of)
					.visual(() -> BlazeMixerVisual::new)
					.validBlocks(BlazingBlocks.BLAZE_MIXER)
					.renderer(() -> BlazeMixerRenderer::new)
					.register();

	public static void register() {

	}

}
