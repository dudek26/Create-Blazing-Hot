package com.dudko.blazinghot.content.block.modern_lamp;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.content.block.shape.AbstractPoint2D;
import com.dudko.blazinghot.content.block.shape.AbstractPoint2D.RelativeOffset;
import com.dudko.blazinghot.content.block.shape.OffsetPoint;
import com.dudko.blazinghot.content.block.shape.Shapes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
public class ModernLampHalfPanelBlock extends ModernLampPanelBlock implements SimpleWaterloggedBlock {

	public static final EnumProperty<RelativeOffset> OFFSET = EnumProperty.create("offset", RelativeOffset.class);

	public ModernLampHalfPanelBlock(Properties properties, DyeColor color) {
		super(properties, color);
		registerDefaultState(defaultBlockState().setValue(OFFSET, RelativeOffset.CENTER_HORIZONTAL));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(OFFSET));
	}

	// TODO: clean this up
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		assert state != null;
		Direction facing = state.getValue(FACING);
		Vec2
				clickedPos =
				AbstractPoint2D.flatten3D(context.getClickLocation().subtract(context.getClickedPos().getCenter()),
						facing.getAxis());
		RelativeOffset offset = AbstractPoint2D.getNearest(OffsetPoint.eightPoints(), clickedPos).offset;
		if (facing.getAxis() == Axis.X && !offset.horizontal) offset = offset.getOpposite();
		if (facing.getAxis() == Axis.Y) {
			if (offset.horizontal) offset = offset.getOpposite();
			if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) offset = offset.getOpposite();
		}
		if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE && !offset.horizontal)
			offset = offset.getOpposite();
		return state.setValue(OFFSET, offset);
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		RelativeOffset offset = state.getValue(OFFSET);
		VoxelShape panel = Shapes.halfPanel(offset.horizontal);
		VoxelShape halfPanelBase = Shapes.halfPanelBase(offset.horizontal);
		double offsetX = offset.horizontal ? 0 : offset.offset;
		double offsetZ = offset.horizontal ? offset.offset : 0;
		return Shapes
				.shape(halfPanelBase.move(offsetX, 0, offsetZ))
				.add(panel.move(offsetX, 0, offsetZ))
				.forDirectional()
				.get(state.getValue(FACING));
	}


}
