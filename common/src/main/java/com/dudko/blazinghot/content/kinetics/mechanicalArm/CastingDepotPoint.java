package com.dudko.blazinghot.content.kinetics.mechanicalArm;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class CastingDepotPoint extends ArmInteractionPoint {

	public CastingDepotPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
	}

	@ExpectPlatform
	public static CastingDepotPoint create(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@Override
	protected Vec3 getInteractionPositionVector() {
		return Vec3.atLowerCornerOf(pos).add(.5f, 12 / 16f, .5f);
	}

}
