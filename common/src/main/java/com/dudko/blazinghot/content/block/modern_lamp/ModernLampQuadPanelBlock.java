package com.dudko.blazinghot.content.block.modern_lamp;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.content.block.shape.Shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModernLampQuadPanelBlock extends ModernLampPanelBlock {
	public ModernLampQuadPanelBlock(Properties properties, DyeColor color) {
		super(properties, color);
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
		return Shapes
				.shape(0, 0, 0, 16, 1, 16)
				.add(1, 0, 1, 7.5, 2, 7.5)
				.add(8.5, 0, 1, 15, 2, 7.5)
				.add(1, 0, 8.5, 7.5, 2, 15)
				.add(8.5, 0, 8.5, 15, 2, 15)
				.forDirectional()
				.get(pState.getValue(FACING));
	}
}
