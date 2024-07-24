package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerInstance;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerRenderer;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.simibubi.create.Create.REGISTRATE;

public class BlazingBlockEntityTypes {

    public static final BlockEntityEntry<BlazeMixerBlockEntity> BLAZE_MIXER = REGISTRATE
            .blockEntity("blaze_mixer", BlazeMixerBlockEntity::new)
            .instance(() -> BlazeMixerInstance::new)
            .validBlocks(BlazingBlocks.BLAZE_MIXER)
            .renderer(() -> BlazeMixerRenderer::new)
            .register();

    public static void register() {

    }
}
