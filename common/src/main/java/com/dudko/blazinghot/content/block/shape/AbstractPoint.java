package com.dudko.blazinghot.content.block.shape;

import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public abstract class AbstractPoint {
	public final Vec3 position;

	public AbstractPoint(Vec3 position) {
		this.position = position;
	}

	public static <T extends AbstractPoint> T getNearest(Collection<T> points, Vec3 position) {
		T nearest = null;
		double nearestDistance = -1;

		for (T point : points) {
			double distance = point.position.distanceTo(position);
			if (distance < nearestDistance || nearestDistance == -1) {
				nearest = point;
				nearestDistance = distance;
			}
		}

		return nearest;
	}

}
