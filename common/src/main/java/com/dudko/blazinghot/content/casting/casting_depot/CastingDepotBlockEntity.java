package com.dudko.blazinghot.content.casting.casting_depot;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CastingDepotBlockEntity extends SmartBlockEntity {

	protected SmartFluidTankBehaviour tank;

	public CastingDepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public abstract void onMoldUpdate();

	@Override
	public void sendData() {
		super.sendData();
		onMoldUpdate();
	}

	public abstract void updateCapacity(long newCapacity);

	public abstract long getCapacity();

	public abstract ItemStack getHeldItem();
}
