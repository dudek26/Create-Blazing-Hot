package com.dudko.blazinghot.content.block.modern_lamp.fabric;

import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlockEntity;
import com.dudko.blazinghot.registry.fabric.BlazingBlockEntityTypesImpl;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModernLampBlockImpl {
	public static BlockEntityType<? extends ModernLampBlockEntity> platformedBlockEntity() {
		return BlazingBlockEntityTypesImpl.MODERN_LAMP.get();
	}
}
