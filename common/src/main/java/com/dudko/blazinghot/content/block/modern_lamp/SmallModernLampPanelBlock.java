package com.dudko.blazinghot.content.block.modern_lamp;

import com.dudko.blazinghot.content.block.shape.AbstractPoint;
import com.dudko.blazinghot.content.block.shape.OffsetPoint;
import com.dudko.blazinghot.content.block.shape.OffsetPoint.Offset;
import com.dudko.blazinghot.content.block.shape.Shapes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.dudko.blazinghot.content.block.shape.Shapes.smallPanel;
import static com.dudko.blazinghot.content.block.shape.Shapes.smallPanelBase;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmallModernLampPanelBlock extends ModernLampPanelBlock {

    public static final EnumProperty<Offset> OFFSET = EnumProperty.create("offset", Offset.class);

    public SmallModernLampPanelBlock(Properties properties, DyeColor color) {
        super(properties, color);
        registerDefaultState(defaultBlockState().setValue(OFFSET, Offset.CENTER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(OFFSET));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        assert state != null;
        Direction facing = state.getValue(FACING);
        Axis axis = facing.getAxis();
        Vec2
                clickedPos =
                AbstractPoint.flatten3D(context.getClickLocation().subtract(context.getClickedPos().getCenter()), axis);
        Offset offset = AbstractPoint.getNearest(OffsetPoint.getPoints(), clickedPos).offset;


        AxisDirection axisDirection = facing.getAxisDirection();
        if ((axis == Axis.X && axisDirection == AxisDirection.POSITIVE) || (axis == Axis.Z
                && axisDirection == AxisDirection.NEGATIVE)) {
            offset = offset.getOpposite(Axis.Y);
        }
        if (axis == Axis.Y && axisDirection == AxisDirection.POSITIVE) {
            offset = offset.getOpposite(Axis.X);
        }
        return state.setValue(OFFSET, offset);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Offset offset = state.getValue(OFFSET);
        Vec2 vec = offset.getShapeOffsetVector();
        return Shapes
                .shape(smallPanelBase().move(vec.x, 0, vec.y))
                .add(smallPanel().move(vec.x, 0, vec.y))
                .forDirectional()
                .get(state.getValue(FACING));
    }
}
