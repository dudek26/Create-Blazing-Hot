package com.dudko.blazinghot.content.block.shape;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractPoint2D {
	public final Vec2 position;

	public AbstractPoint2D(Vec2 position) {
		this.position = position;
	}

	public static <T extends AbstractPoint2D> T getNearest(Collection<T> points, Vec2 position) {
		T nearest = null;
		double nearestDistance = -1;

		for (T point : points) {
			double distance = Mth.sqrt(point.position.distanceToSqr(position));
			if (distance < nearestDistance || nearestDistance == -1) {
				nearest = point;
				nearestDistance = distance;
			}
		}

		return nearest;
	}

	public static Vec2 flatten3D(Vec3 vec3, Axis ignoredAxis) {
		return switch (ignoredAxis) {
			case X -> new Vec2(f(vec3.z), f(vec3.y));
			case Y -> new Vec2(f(vec3.x), f(vec3.z));
			case Z -> new Vec2(f(vec3.x), f(vec3.y));
		};
	}

	protected static float f(double value) {
		return (float) value;
	}

	public enum RelativeOffset implements StringRepresentable {
		UP("up", "u", true),
		LEFT("left", "l", false),
		RIGHT("right", "r", false, 0.5),
		DOWN("down", "d", true, 0.5),
		CENTER_HORIZONTAL("center_horizontal", "h", true, 0.25),
		CENTER_VERTICAL("center_vertical", "v", false, 0.25);

		private final String name;
		public final String shortName;
		public final boolean horizontal;
		public final double offset;

		RelativeOffset(String name, String shortName, boolean horizontal, double offset) {
			this.name = name;
			this.shortName = shortName;
			this.horizontal = horizontal;
			this.offset = offset;
		}

		RelativeOffset(String name, String shortName, boolean horizontal) {
			this.name = name;
			this.shortName = shortName;
			this.horizontal = horizontal;
			this.offset = 0;
		}

		@Override
		public @NotNull String getSerializedName() {
			return name;
		}

		public RelativeOffset getOpposite() {
			return switch (this) {
				case UP -> DOWN;
				case LEFT -> RIGHT;
				case RIGHT -> LEFT;
				case DOWN -> UP;
				default -> this;
			};
		}
	}
}
