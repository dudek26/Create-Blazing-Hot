package com.dudko.blazinghot.content.block.shape;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OffsetPoint extends AbstractPoint {

	public final Offset offset;

	public OffsetPoint(Offset offset) {
		super(offset.getVector());
		this.offset = offset != Offset.RIGHT && offset != Offset.LEFT ? offset.getOpposite(Axis.X) : offset;
	}

	public static List<OffsetPoint> getPoints() {
		return Stream.of(Offset.values()).map(OffsetPoint::new).toList();
	}

	public enum Offset implements StringRepresentable {
		UP_RIGHT("up_right", 0.5, -0.5),
		RIGHT("right", 0.5, 0),
		DOWN_RIGHT("down_right", 0.5, 0.5),
		DOWN_LEFT("down_left", -0.5, 0.5),
		LEFT("left", -0.5, 0),
		UP_LEFT("up_left", -0.5, -0.5),
		UP("up", 0, -0.5),
		DOWN("down", 0, 0.5),
		CENTER("center", 0, 0);

		private final String name;
		public final double x;
		public final double y;

		Offset(String name, double x, double y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}

		public Vec2 getVector() {
			return new Vec2(f(x), f(y));
		}

		public Vec2 getShapeOffsetVector() {
			return new Vec2(f(x + 0.5) / 2, f(y + 0.5) / 2);
		}

		public String getShortName() {
			String[] split = name.split("_");
			StringBuilder builder = new StringBuilder();
			for (String s : split) {
				builder.append(s.charAt(0));
			}
			return builder.toString();
		}

		public Offset getOpposite() {
			return getOpposite(Axis.X).getOpposite(Axis.Y);
		}

		public Offset getOpposite(Axis axis) {
			if (axis == Axis.X) {
				return switch (this) {
					case UP_RIGHT -> DOWN_RIGHT;
					case RIGHT -> RIGHT;
					case DOWN_RIGHT -> UP_RIGHT;
					case DOWN_LEFT -> UP_LEFT;
					case LEFT -> LEFT;
					case UP_LEFT -> DOWN_LEFT;
					case UP -> DOWN;
					case DOWN -> UP;
					case CENTER -> CENTER;
				};
			}
			else if (axis == Axis.Y) {
				return switch (this) {
					case UP_RIGHT -> UP_LEFT;
					case RIGHT -> LEFT;
					case DOWN_RIGHT -> DOWN_LEFT;
					case DOWN_LEFT -> DOWN_RIGHT;
					case LEFT -> RIGHT;
					case UP_LEFT -> UP_RIGHT;
					case UP -> UP;
					case DOWN -> DOWN;
					case CENTER -> CENTER;
				};
			}
			else return this;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

}
