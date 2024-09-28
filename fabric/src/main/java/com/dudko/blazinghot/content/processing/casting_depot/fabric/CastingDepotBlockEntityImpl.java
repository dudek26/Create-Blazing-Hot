package com.dudko.blazinghot.content.processing.casting_depot.fabric;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.processing.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("UnstableApiUsage")
public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity implements SidedStorageBlockEntity {
	public CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, MultiFluids.Constants.BUCKET.platformed());
		tank.whenFluidUpdates(() -> {

		});
		behaviours.add(tank);
	}

	@Override
	public @Nullable Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
		if (face != Direction.UP) {
			return tank.getCapability();
		}
		return null;
	}

	@Override
	public @Nullable Storage<ItemVariant> getItemStorage(@Nullable Direction side) {
		return SidedStorageBlockEntity.super.getItemStorage(side);
	}
}
