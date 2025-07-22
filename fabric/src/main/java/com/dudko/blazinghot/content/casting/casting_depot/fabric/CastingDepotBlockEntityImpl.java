package com.dudko.blazinghot.content.casting.casting_depot.fabric;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings("UnstableApiUsage")
public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity implements SidedStorageBlockEntity {

	protected CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private CastingDepotBehaviourImpl getInputBehaviour() {
		return (CastingDepotBehaviourImpl) inputBehaviour;
	}

	@Override
	public ItemStack getHeldItem() {
		return getInputBehaviour().heldItem == null ? ItemStack.EMPTY : getInputBehaviour().heldItem.stack;
	}

	@Override
	public ItemStack getOutputItem() {
		return null;
	}

	@Override
	public float getCoolingSpeed() {
		return 1 + inputBehaviour.coolingSpeedModifier;
	}

	@Override
	public void setFluid(Fluid fluid, long amount) {

	}

	@Override
	public void resetFluid() {

	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);

	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction direction) {
		return getInputBehaviour().itemHandler;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return containedFluidTooltip(tooltip, isPlayerSneaking, null);
	}

	public static CastingDepotBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CastingDepotBlockEntityImpl(type, pos, state);
	}
}
