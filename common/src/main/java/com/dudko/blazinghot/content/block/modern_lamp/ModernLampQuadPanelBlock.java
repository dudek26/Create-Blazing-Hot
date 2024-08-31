package com.dudko.blazinghot.content.block.modern_lamp;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.block.shape.Shapes;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.util.DyeUtil;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
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

	private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

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
		if (!player.isShiftKeyDown() && player.mayBuild()) {
			ItemStack heldItem = player.getItemInHand(hand);
			IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
			if (helper.matchesItem(heldItem)) return helper
					.getOffset(player, level, state, pos, hit)
					.placeInWorld(level, (BlockItem) heldItem.getItem(), player, hand, hit);
		}

		return super.use(state, level, pos, player, hand, hit);
	}

	private static class PlacementHelper implements IPlacementHelper {
		@Override
		public Predicate<ItemStack> getItemPredicate() {
			return stack -> DyeUtil.isIn(BlazingBlocks.MODERN_LAMP_QUAD_PANELS, stack);
		}

		@Override
		public Predicate<BlockState> getStatePredicate() {
			return state -> BlazingBlocks.MODERN_LAMP_QUAD_PANELS.contains(state.getBlock());
		}

		@Override
		public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
			List<Direction>
					directions =
					IPlacementHelper.orderedByDistanceExceptAxis(pos,
							ray.getLocation(),
							state.getValue(FACING).getAxis(),
							dir -> world.getBlockState(pos.relative(dir)).canBeReplaced());

			if (directions.isEmpty()) return PlacementOffset.fail();
			else {
				return PlacementOffset.success(pos.relative(directions.get(0)),
						s -> s.setValue(FACING, state.getValue(FACING)));
			}
		}
	}
}
