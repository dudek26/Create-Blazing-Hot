package com.dudko.blazinghot.content.block.shape;

import com.simibubi.create.AllShapes;

import net.minecraft.world.level.block.Block;
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

	public static VoxelShape halfDoublePanel(boolean horizontal) {
		return horizontal ? cuboid(1, 1, 1, 15, 2, 7.5) : cuboid(1, 1, 1, 7.5, 2, 15);
	}

	public static VoxelShape halfPanel(boolean horizontal) {
		return horizontal ? cuboid(1, 1, 1, 15, 2, 7) : cuboid(1, 1, 1, 7, 2, 15);
	}

	public static VoxelShape halfPanelBase(boolean horizontal) {
		return horizontal ? cuboid(0, 0, 0, 16, 1, 8) : cuboid(0, 0, 0, 8, 1, 16);
	}

}
