package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerInstance;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.dudko.blazinghot.BlazingHot.REGISTRATE;


public class BlazingBlockEntityTypes {

    public static final BlockEntityEntry<BlazeMixerBlockEntity> BLAZE_MIXER = REGISTRATE
            .blockEntity("blaze_mixer",
                    BlazeMixerBlockEntity::new)
            .instance(() -> BlazeMixerInstance::new)
            .validBlocks(BlazingBlocks.BLAZE_MIXER)
            .renderer(() -> BlazeMixerRenderer::new)
            .register();

    public static void register() {

    }
}
