package com.dudko.blazinghot.registry.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotRenderer;
import com.dudko.blazinghot.content.casting.casting_depot.fabric.CastingDepotBlockEntityImpl;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerInstance;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerRenderer;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.fabric.BlazeMixerBlockEntityImpl;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class BlazingBlockEntityTypesImpl {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static final BlockEntityEntry<BlazeMixerBlockEntityImpl>
			BLAZE_MIXER =
			REGISTRATE
					.blockEntity("blaze_mixer", BlazeMixerBlockEntityImpl::new)
					.instance(() -> BlazeMixerInstance::new)
					.validBlocks(BlazingBlocks.BLAZE_MIXER)
					.renderer(() -> BlazeMixerRenderer::new)
					.register();

	public static final BlockEntityEntry<CastingDepotBlockEntityImpl>
			CASTING_DEPOT =
			REGISTRATE
					.blockEntity("casting_depot", CastingDepotBlockEntityImpl::new)
					.validBlocks(BlazingBlocks.CASTING_DEPOT)
					.renderer(() -> CastingDepotRenderer::new)
					.register();

	public static void platformRegister() {
	}
}
