package com.dudko.blazinghot.content.block.shape;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;

public class DirectionOffsetPoint extends AbstractPoint {

	public final DirectionOffset offset;

	public DirectionOffsetPoint(Vec2 position, DirectionOffset offset) {
		super(position);
		this.offset = offset;
	}

	public static List<DirectionOffsetPoint> eightPoints() {
		List<DirectionOffsetPoint> points = new ArrayList<>();
		points.add(new DirectionOffsetPoint(new Vec2(0, 0.01f), DirectionOffset.CENTER_HORIZONTAL));
		points.add(new DirectionOffsetPoint(new Vec2(0, -0.01f), DirectionOffset.CENTER_HORIZONTAL));
		points.add(new DirectionOffsetPoint(new Vec2(0.01f, 0), DirectionOffset.CENTER_VERTICAL));
		points.add(new DirectionOffsetPoint(new Vec2(-0.01f, 0), DirectionOffset.CENTER_VERTICAL));
		points.add(new DirectionOffsetPoint(new Vec2(0.5f, 0), DirectionOffset.RIGHT));
		points.add(new DirectionOffsetPoint(new Vec2(-0.5f, 0), DirectionOffset.LEFT));
		points.add(new DirectionOffsetPoint(new Vec2(0, 0.5f), DirectionOffset.UP));
		points.add(new DirectionOffsetPoint(new Vec2(0, -0.5f), DirectionOffset.DOWN));
		return points;
	}

	public static List<DirectionOffsetPoint> fourPoints() {
		List<DirectionOffsetPoint> points = new ArrayList<>();
		points.add(new DirectionOffsetPoint(new Vec2(0.5f, 0), DirectionOffset.RIGHT));
		points.add(new DirectionOffsetPoint(new Vec2(-0.5f, 0), DirectionOffset.LEFT));
		points.add(new DirectionOffsetPoint(new Vec2(0, 0.5f), DirectionOffset.UP));
		points.add(new DirectionOffsetPoint(new Vec2(0, -0.5f), DirectionOffset.DOWN));
		return points;
	}

	public enum DirectionOffset implements StringRepresentable {
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

		DirectionOffset(String name, String shortName, boolean horizontal, double offset) {
			this.name = name;
			this.shortName = shortName;
			this.horizontal = horizontal;
			this.offset = offset;
		}

		DirectionOffset(String name, String shortName, boolean horizontal) {
			this.name = name;
			this.shortName = shortName;
			this.horizontal = horizontal;
			this.offset = 0;
		}

		@Override
		public @NotNull String getSerializedName() {
			return name;
		}

		public DirectionOffset getOpposite() {
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
