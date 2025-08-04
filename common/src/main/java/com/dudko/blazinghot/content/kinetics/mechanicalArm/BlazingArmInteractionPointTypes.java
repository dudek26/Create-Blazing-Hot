package com.dudko.blazinghot.content.kinetics.mechanicalArm;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlazingArmInteractionPointTypes {

	static {
		register("casting_depot", new CastingDepotType());
	}

	private static <T extends ArmInteractionPointType> void register(String name, T type) {
		Registry.register(CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE, BlazingHot.asResource(name), type);
	}

	public static void init() {
	}

	public static class CastingDepotType extends ArmInteractionPointType {

		@Override
		public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
			return BlazingBlocks.CASTING_DEPOT.has(state);
		}

		@Override
		public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
			return CastingDepotPoint.create(this, level, pos, state);
		}
	}

}
