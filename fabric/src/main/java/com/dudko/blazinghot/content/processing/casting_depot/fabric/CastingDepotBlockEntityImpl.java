package com.dudko.blazinghot.content.processing.casting_depot.fabric;

import java.util.List;

import com.dudko.blazinghot.content.processing.casting_depot.CastingDepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity {
	public CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

	}
}
