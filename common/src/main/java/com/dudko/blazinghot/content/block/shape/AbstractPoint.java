package com.dudko.blazinghot.content.block.shape;

import java.util.Collection;

import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractPoint {
	public final Vec2 position;

	public AbstractPoint(Vec2 position) {
		this.position = position;
	}

	public static <T extends AbstractPoint> T getNearest(Collection<T> points, Vec2 position) {
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

}
