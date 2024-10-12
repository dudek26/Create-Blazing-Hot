package com.dudko.blazinghot.content.casting.casting_depot.fabric;

import static com.dudko.blazinghot.content.casting.Molds.getMoldCapacity;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.multiloader.MultiFluids.Constants;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("UnstableApiUsage")
public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity implements SidedStorageBlockEntity, IHaveGoggleInformation {

	CastingDepotBehaviour inputBehaviour;

	public CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void onMoldUpdate() {
		ItemStack stack = inputBehaviour.heldItem == null ? ItemStack.EMPTY : inputBehaviour.heldItem.stack;
		long newCapacity = getMoldCapacity(stack);
		if (getCapacity() != newCapacity) updateCapacity(newCapacity);
	}

	@Override
	public void updateCapacity(long capacity) {
		tank.getPrimaryTank().getTank().setCapacity(capacity);
		tank.sendDataImmediately();
	}

	@Override
	public long getCapacity() {
		return tank.getPrimaryTank().getTank().getCapacity();
	}

	@Override
	public ItemStack getHeldItem() {
		return inputBehaviour.heldItem == null ? ItemStack.EMPTY : inputBehaviour.heldItem.stack;
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, Constants.INGOT.platformed() * 2);
		tank.whenFluidUpdates(() -> {

		});
		behaviours.add(tank);

		behaviours.add(inputBehaviour = new CastingDepotBehaviour(this, CastingDepotBehaviour.INPUT));
		inputBehaviour.addSubBehaviours(behaviours);
	}

	@Override
	public long getFluidAmount() {
		return tank.getPrimaryTank().getTank().getFluidAmount();
	}

	@Override
	public @Nullable Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
		if (face != Direction.UP) {
			return tank.getCapability();
		}
		return null;
	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction direction) {
		return inputBehaviour.itemHandler;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return containedFluidTooltip(tooltip, isPlayerSneaking, getFluidStorage(null));
	}
}
