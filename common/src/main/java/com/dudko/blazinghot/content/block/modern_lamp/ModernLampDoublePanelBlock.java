package com.dudko.blazinghot.content.block.modern_lamp;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.content.block.shape.AbstractPoint;
import com.dudko.blazinghot.content.block.shape.DirectionPoint;
import com.dudko.blazinghot.content.block.shape.Shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
public class ModernLampDoublePanelBlock extends ModernLampPanelBlock {
	public static final BooleanProperty HORIZONTAL = BooleanProperty.create("horizontal");

	public ModernLampDoublePanelBlock(Properties properties, DyeColor color) {
		super(properties, color);
		registerDefaultState(defaultBlockState().setValue(HORIZONTAL, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(HORIZONTAL));
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		boolean isHorizontal = state.getValue(HORIZONTAL);
		VoxelShape panel = Shapes.halfPanel(isHorizontal);
		double offsetX = isHorizontal ? 0 : 7.5 / 16;
		double offsetZ = isHorizontal ? 7.5 / 16 : 0;
		return Shapes
				.shape(0, 0, 0, 16, 1, 16)
				.add(panel)
				.add(panel.move(offsetX, 0, offsetZ))
				.forDirectional()
				.get(state.getValue(FACING));
	}


	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		assert state != null;
		Vec3 clickedPos = context.getClickLocation().subtract(context.getClickedPos().getCenter());
		Direction facing = state.getValue(FACING);
		return state.setValue(HORIZONTAL,
				AbstractPoint
						.getNearest(DirectionPoint.simplePoints(facing.getAxis()), clickedPos)
						.isHorizontal(facing));
	}
}
