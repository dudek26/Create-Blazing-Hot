package com.dudko.blazinghot.content.block.shape;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.phys.Vec2;

public class OffsetPoint extends AbstractPoint2D {

	public final RelativeOffset offset;

	public OffsetPoint(Vec2 position, RelativeOffset offset) {
		super(position);
		this.offset = offset;
	}

	public static List<OffsetPoint> eightPoints() {
		List<OffsetPoint> points = new ArrayList<>();
		points.add(new OffsetPoint(new Vec2(0, 0.01f), RelativeOffset.CENTER_HORIZONTAL));
		points.add(new OffsetPoint(new Vec2(0, -0.01f), RelativeOffset.CENTER_HORIZONTAL));
		points.add(new OffsetPoint(new Vec2(0.01f, 0), RelativeOffset.CENTER_VERTICAL));
		points.add(new OffsetPoint(new Vec2(-0.01f, 0), RelativeOffset.CENTER_VERTICAL));
		points.add(new OffsetPoint(new Vec2(0.5f, 0), RelativeOffset.RIGHT));
		points.add(new OffsetPoint(new Vec2(-0.5f, 0), RelativeOffset.LEFT));
		points.add(new OffsetPoint(new Vec2(0, 0.5f), RelativeOffset.UP));
		points.add(new OffsetPoint(new Vec2(0, -0.5f), RelativeOffset.DOWN));
		return points;
	}

	public static List<OffsetPoint> fourPoints() {
		List<OffsetPoint> points = new ArrayList<>();
		points.add(new OffsetPoint(new Vec2(0.5f, 0), RelativeOffset.RIGHT));
		points.add(new OffsetPoint(new Vec2(-0.5f, 0), RelativeOffset.LEFT));
		points.add(new OffsetPoint(new Vec2(0, 0.5f), RelativeOffset.UP));
		points.add(new OffsetPoint(new Vec2(0, -0.5f), RelativeOffset.DOWN));
		return points;
	}
}
