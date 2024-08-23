package com.dudko.blazinghot.content.kinetics.blaze_mixer.fabric;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.registry.fabric.BlazingBlockEntityTypesImpl;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlazeMixerBlockImpl {

	@SuppressWarnings("unchecked")
	public static <T extends BlazeMixerBlockEntity> BlockEntityType<T> platformedBlockEntity() {
		return (BlockEntityType<T>) BlazingBlockEntityTypesImpl.BLAZE_MIXER.get();
	}

}
