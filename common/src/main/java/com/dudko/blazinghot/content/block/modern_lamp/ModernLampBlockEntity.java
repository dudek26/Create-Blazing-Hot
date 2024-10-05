package com.dudko.blazinghot.content.block.modern_lamp;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ModernLampBlockEntity extends BlockEntity {

	public boolean powered;
	public boolean locked;

	public ModernLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

}
