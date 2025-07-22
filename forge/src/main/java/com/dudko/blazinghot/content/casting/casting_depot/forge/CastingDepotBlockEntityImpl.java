package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity {

	protected CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private CastingDepotBehaviourImpl getInputBehaviour() {
		return (CastingDepotBehaviourImpl) inputBehaviour;
	}

	@Override
	public ItemStack getHeldItem() {
		return inputBehaviour.heldStack == null ? ItemStack.EMPTY : inputBehaviour.heldStack;
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
		tank.getPrimaryHandler().setFluid(new FluidStack(fluid, (int) amount));
	}

	@Override
	public void resetFluid() {
		tank.getPrimaryHandler().setFluid(FluidStack.EMPTY);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);

		tank = SmartFluidTankBehaviour.single(this, (int) MultiAmount.BLOCK.multiply(4).get());
		behaviours.add(tank);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (isItemHandlerCap(cap)) {
			return this.getInputBehaviour().getItemCapability();
		}
		return super.getCapability(cap, side);
	}

	public static CastingDepotBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CastingDepotBlockEntityImpl(type, pos, state);
	}

}
