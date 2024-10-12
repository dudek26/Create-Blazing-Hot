package com.dudko.blazinghot.content.block.modern_lamp;

import static com.dudko.blazinghot.content.block.shape.Shapes.smallPanel;
import static com.dudko.blazinghot.content.block.shape.Shapes.smallPanelBase;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.block.shape.OffsetPoint.Offset;
import com.dudko.blazinghot.content.block.shape.Shapes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmallModernLampPanelBlock extends AbstractModernLampPanel {

	public SmallModernLampPanelBlock(Properties properties, DyeColor color) {
		super(properties, color);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Offset offset = Offset.CENTER;
		Vec2 vec = offset.getShapeOffsetVector();
		return Shapes
				.shape(smallPanelBase().move(vec.x, 0, vec.y))
				.add(smallPanel().move(vec.x, 0, vec.y))
				.forDirectional()
				.get(state.getValue(FACING));
	}

}
