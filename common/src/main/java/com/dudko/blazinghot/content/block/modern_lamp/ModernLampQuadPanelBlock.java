package com.dudko.blazinghot.content.block.modern_lamp;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.block.shape.Shapes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModernLampQuadPanelBlock extends ModernLampPanelBlock {
	public ModernLampQuadPanelBlock(Properties properties, DyeColor color) {
		super(properties, color);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return Shapes
				.shape(0, 0, 0, 16, 1, 16)
				.add(1, 0, 1, 7.5, 2, 7.5)
				.add(8.5, 0, 1, 15, 2, 7.5)
				.add(1, 0, 8.5, 7.5, 2, 15)
				.add(8.5, 0, 8.5, 15, 2, 15)
				.forDirectional()
				.get(pState.getValue(FACING));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return super.use(state, level, pos, player, hand, hit);
	}

}
