package com.dudko.blazinghot.content.block.shape;

import com.simibubi.create.AllShapes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Shapes {

	public static final VoxelShape
			CASTING_DEPOT_SHAPE =
			shape(AllShapes.CASING_12PX.get(Direction.UP)).erase(1, 11, 1, 15, 12, 15).build();

	public static AllShapes.Builder shape(VoxelShape shape) {
		return new AllShapes.Builder(shape);
	}

	public static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
		return shape(cuboid(x1, y1, z1, x2, y2, z2));
	}

	public static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Block.box(x1, y1, z1, x2, y2, z2);
	}

	public static VoxelShape halfDoublePanel(boolean horizontal) {
		return horizontal ? cuboid(1, 1, 1, 15, 2, 7.5) : cuboid(1, 1, 1, 7.5, 2, 15);
	}

	public static VoxelShape halfPanel(boolean horizontal) {
		return horizontal ? cuboid(1, 1, 5, 15, 2, 11) : cuboid(5, 1, 1, 11, 2, 15);
	}

	public static VoxelShape halfPanelBase(boolean horizontal) {
		return horizontal ? cuboid(0, 0, 4, 16, 1, 12) : cuboid(4, 0, 0, 12, 1, 16);
	}

	public static VoxelShape smallPanelBase() {
		return cuboid(0, 0, 0, 8, 1, 8);
	}

	public static VoxelShape smallPanel() {
		return cuboid(1, 1, 1, 7, 2, 7);
	}

}
