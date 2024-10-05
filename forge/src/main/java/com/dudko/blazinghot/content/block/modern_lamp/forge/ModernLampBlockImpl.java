package com.dudko.blazinghot.content.block.modern_lamp.forge;

import com.dudko.blazinghot.content.block.modern_lamp.block.ModernLampBlockEntity;
import com.dudko.blazinghot.registry.forge.BlazingBlockEntityTypesImpl;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModernLampBlockImpl {

	public static BlockEntityType<? extends ModernLampBlockEntity> platformedBlockEntity() {
		return BlazingBlockEntityTypesImpl.MODERN_LAMP.get();
	}

}
