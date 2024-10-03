package com.dudko.blazinghot.content.casting.casting_depot.fabric;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.registry.fabric.BlazingBlockEntityTypesImpl;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class CastingDepotBlockImpl {
	@SuppressWarnings("unchecked")
	public static <T extends CastingDepotBlockEntity> BlockEntityType<T> platformedBlockEntity() {
		return (BlockEntityType<T>) BlazingBlockEntityTypesImpl.CASTING_DEPOT.get();
	}
}
