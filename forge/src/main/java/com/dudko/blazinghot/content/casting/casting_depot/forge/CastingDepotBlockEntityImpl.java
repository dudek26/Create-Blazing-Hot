package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity {

	CastingDepotBehaviourImpl inputBehaviour;

	protected CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void onMoldUpdate() {

	}

	@Override
	public ItemStack getHeldItem() {
		return inputBehaviour.heldItem == null ? ItemStack.EMPTY : inputBehaviour.heldItem.stack;
	}

	@Override
	public float getCoolingSpeed() {
		return 1 + inputBehaviour.coolingSpeedModifier;
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(inputBehaviour = new CastingDepotBehaviourImpl(this, CastingDepotBehaviour.INPUT));
		inputBehaviour.addSubBehaviours(behaviours);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (isItemHandlerCap(cap)) {
			return this.inputBehaviour.getItemCapability(cap, side);
		}
		return super.getCapability(cap, side);
	}

	public static CastingDepotBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CastingDepotBlockEntityImpl(type, pos, state);
	}
}
