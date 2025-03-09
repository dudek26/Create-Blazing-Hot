package com.dudko.blazinghot.util;

import java.util.Map;

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

}
