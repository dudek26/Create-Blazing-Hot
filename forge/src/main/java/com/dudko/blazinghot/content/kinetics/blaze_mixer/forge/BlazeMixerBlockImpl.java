package com.dudko.blazinghot.content.kinetics.blaze_mixer.forge;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.registry.forge.BlazingBlockEntityTypesImpl;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlazeMixerBlockImpl {
	@SuppressWarnings("unchecked")
	public static <T extends BlazeMixerBlockEntity> BlockEntityType<T> platformedBlockEntity() {
		return (BlockEntityType<T>) BlazingBlockEntityTypesImpl.BLAZE_MIXER.get();
	}
}
