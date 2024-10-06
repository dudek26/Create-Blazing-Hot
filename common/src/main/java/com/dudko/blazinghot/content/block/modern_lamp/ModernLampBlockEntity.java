package com.dudko.blazinghot.content.block.modern_lamp;

import java.util.List;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ModernLampBlockEntity extends BlockEntity implements IHaveGoggleInformation {

	public boolean powered;
	public boolean locked;

	public ModernLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putBoolean("powered", powered);
		tag.putBoolean("locked", locked);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		powered = tag.getBoolean("powered");
		locked = tag.getBoolean("locked");
		super.load(tag);
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return IHaveGoggleInformation.super.addToGoggleTooltip(tooltip, isPlayerSneaking);
	}
}
