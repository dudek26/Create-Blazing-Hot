package com.dudko.blazinghot.util;

import java.util.Arrays;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class DirectionUtil {

	public static Map<Direction, Double>
			HORIZONTAL_ANGLES =
			Map.of(Direction.NORTH,
					Math.toRadians(180),
					Direction.EAST,
					Math.toRadians(270),
					Direction.SOUTH,
					0d,
					Direction.WEST,
					Math.toRadians(90));
	public static Direction getNeighbouringDirection(BlockPos pos1, BlockPos pos2) {
		for (Direction direction : Direction.values()) {
			if (pos1.relative(direction).equals(pos2)) {
				return direction;
			}
		}
		return null;
	}

	public static Direction[] allBut(Direction direction) {
		return Arrays.stream(Direction.values()).filter(dir -> dir != direction).toArray(Direction[]::new);
	}

}
