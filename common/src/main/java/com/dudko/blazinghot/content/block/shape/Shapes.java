package com.dudko.blazinghot.content.block.shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.simibubi.create.AllShapes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Shapes {

	public static AllShapes.Builder shape(VoxelShape shape) {
		return new AllShapes.Builder(shape);
	}

	public static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
		return shape(cuboid(x1, y1, z1, x2, y2, z2));
	}

	public static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Block.box(x1, y1, z1, x2, y2, z2);
	}

	public static VoxelShape halfPanel(boolean horizontal) {
		return horizontal ? cuboid(1, 1, 1, 15, 2, 7.5) : cuboid(1, 1, 1, 7.5, 2, 15);
	}


	public static class AxisPoint {
		public final Direction direction;
		public final Vec3 position;

		public AxisPoint(Vec3 position, Direction direction) {
			this.direction = direction;
			this.position = position;
		}

		public static AxisPoint getNearest(Collection<AxisPoint> points, Vec3 position) {
			AxisPoint nearest = null;
			double nearestDistance = -1;

			for (AxisPoint point : points) {
				double distance = point.position.distanceTo(position);
				if (distance < nearestDistance || nearestDistance == -1) {
					nearest = point;
					nearestDistance = distance;
				}
			}

			return nearest;
		}

		public static List<AxisPoint> simplePoints(Direction.Axis... ignoredAxes) {
			List<AxisPoint> points = new ArrayList<>();
			addPoint(points, 0, 0, -0.5, Direction.SOUTH, ignoredAxes);
			addPoint(points, 0.5, 0, 0, Direction.EAST, ignoredAxes);
			addPoint(points, 0, 0, 0.5, Direction.NORTH, ignoredAxes);
			addPoint(points, -0.5, 0, 0, Direction.WEST, ignoredAxes);
			addPoint(points, 0, 0.5, 0, Direction.UP, ignoredAxes);
			addPoint(points, 0, -0.5, 0, Direction.WEST, ignoredAxes);
			return points;
		}

		private static void addPoint(List<AxisPoint> points, double x, double y, double z, Direction direction, Direction.Axis... ignoredAxes) {
			List<Direction.Axis> ignoredAxesList = List.of(ignoredAxes);
			if (!ignoredAxesList.contains(direction.getAxis())) points.add(new AxisPoint(new Vec3(x, y, z), direction));
		}

	}
}
