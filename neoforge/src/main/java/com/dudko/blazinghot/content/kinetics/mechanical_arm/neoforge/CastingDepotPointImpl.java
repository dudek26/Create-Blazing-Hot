package com.dudko.blazinghot.content.kinetics.mechanical_arm.neoforge;

import com.dudko.blazinghot.content.kinetics.mechanical_arm.CastingDepotPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CastingDepotPointImpl extends CastingDepotPoint {

	public CastingDepotPointImpl(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
	}

	@Override
	public ItemStack insert(ItemStack stack, boolean simulate) {
		return super.insert(stack, simulate);
	}

	@Override
	public ItemStack extract(int slot, int amount, boolean simulate) {
		return super.extract(slot, amount, simulate);
	}

	public static CastingDepotPoint create(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		return new CastingDepotPointImpl(type, level, pos, state);
	}
}
