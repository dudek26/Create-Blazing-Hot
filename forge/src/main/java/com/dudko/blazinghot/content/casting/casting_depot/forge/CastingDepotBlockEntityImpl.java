package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.List;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dudko.blazinghot.content.casting.Molds.getMoldCapacity;

public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity implements IHaveGoggleInformation {

	CastingDepotBehaviour inputBehaviour;

	protected CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
		tank.getPrimaryHandler().setCapacity((int) capacity);
		tank.sendDataImmediately();
	}

	@Override
	public long getCapacity() {
		return tank.getPrimaryHandler().getCapacity();
	}

	@Override
	public ItemStack getHeldItem() {
		return inputBehaviour.heldItem == null ? ItemStack.EMPTY : inputBehaviour.heldItem.stack;
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, (int) MultiAmount.INGOT.get() * 2);
		tank.whenFluidUpdates(() -> {

		});
		behaviours.add(tank);

		behaviours.add(inputBehaviour = new CastingDepotBehaviour(this, CastingDepotBehaviour.INPUT));
		inputBehaviour.addSubBehaviours(behaviours);
	}

	@Override
	public long getFluidAmount() {
		return tank.getPrimaryHandler().getFluidAmount();
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (isItemHandlerCap(cap)) {
			return this.inputBehaviour.getItemCapability(cap, side);
		}
		return side != Direction.UP && this.isFluidHandlerCap(cap) ? this.tank.getCapability().cast() : super.getCapability(cap, side);
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return containedFluidTooltip(tooltip, isPlayerSneaking, this.tank.getCapability().cast());
	}

	public static CastingDepotBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CastingDepotBlockEntityImpl(type, pos, state);
	}
}
