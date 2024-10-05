package com.dudko.blazinghot.content.block.modern_lamp;

import com.dudko.blazinghot.content.block.modern_lamp.block.ModernLampBlockEntity;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IModernLampBE extends IBE<ModernLampBlockEntity> {

	@Override
	default Class<ModernLampBlockEntity> getBlockEntityClass() {
		return ModernLampBlockEntity.class;
	}

	default boolean iIsLocked(Level level, BlockPos pos) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		return be != null && be.locked;
	}

	default boolean iIsPowered(Level level, BlockPos pos) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		return be != null && be.powered;
	}

	default void iSetLocked(Level level, BlockPos pos, boolean locked) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		if (be != null) be.locked = locked;
	}

	default void iSetPowered(Level level, BlockPos pos, boolean powered) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		if (be != null) be.powered = powered;
	}
}
