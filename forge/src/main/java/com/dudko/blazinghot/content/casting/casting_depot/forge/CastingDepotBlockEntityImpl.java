package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.List;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity {
	public CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void onMoldUpdate() {

	}

	@Override
	public void updateCapacity(long newCapacity) {

	}

	@Override
	public long getCapacity() {
		return 0;
	}

	@Override
	public ItemStack getHeldItem() {
		return null;
	}

	@Override
	public long getFluidAmount() {
		return 0;
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

	}
}
