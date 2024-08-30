package com.dudko.blazinghot.content.block.shape;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class DirectionPoint extends AbstractPoint {

	public final Direction direction;

	public DirectionPoint(Vec3 position, Direction direction) {
		super(position);
		this.direction = direction;
	}

	public DirectionPoint(double x, double y, double z, Direction direction) {
		this(new Vec3(x, y, z), direction);
	}

	public boolean isHorizontal(Direction facing) {
		Direction.Axis axis = facing.getAxis();
		List<Direction> horizontals = new ArrayList<>(List.of(Direction.UP, Direction.DOWN));
		if (axis.getPlane() == Direction.Plane.VERTICAL) {
			horizontals.addAll(List.of(Direction.NORTH, Direction.SOUTH));
		}
		return horizontals.contains(this.direction);
	}

	public static List<DirectionPoint> simplePoints(Direction.Axis... ignoredAxes) {
		List<DirectionPoint> points = new ArrayList<>();
		addPoint(points, 0, 0, -0.5, Direction.SOUTH, ignoredAxes);
		addPoint(points, 0.5, 0, 0, Direction.EAST, ignoredAxes);
		addPoint(points, 0, 0, 0.5, Direction.NORTH, ignoredAxes);
		addPoint(points, -0.5, 0, 0, Direction.WEST, ignoredAxes);
		addPoint(points, 0, 0.5, 0, Direction.UP, ignoredAxes);
		addPoint(points, 0, -0.5, 0, Direction.DOWN, ignoredAxes);
		return points;
	}

	private static void addPoint(List<DirectionPoint> points, double x, double y, double z, Direction direction, Direction.Axis... ignoredAxes) {
		List<Direction.Axis> ignoredAxesList = List.of(ignoredAxes);
		if (!ignoredAxesList.contains(direction.getAxis())) points.add(new DirectionPoint(x, y, z, direction));
	}

}
