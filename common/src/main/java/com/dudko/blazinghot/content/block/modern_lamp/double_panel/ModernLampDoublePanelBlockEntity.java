package com.dudko.blazinghot.content.block.modern_lamp.double_panel;

import com.dudko.blazinghot.content.block.modern_lamp.block.ModernLampBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ModernLampDoublePanelBlockEntity extends ModernLampBlockEntity {

	public boolean horizontal;

	public ModernLampDoublePanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putBoolean("horizontal", horizontal);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		horizontal = tag.getBoolean("horizontal");
		super.load(tag);
	}
}
