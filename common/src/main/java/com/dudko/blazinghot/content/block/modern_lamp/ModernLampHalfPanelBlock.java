package com.dudko.blazinghot.content.block.modern_lamp;

import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.content.block.shape.AbstractPoint;
import com.dudko.blazinghot.content.block.shape.DirectionOffsetPoint;
import com.dudko.blazinghot.content.block.shape.DirectionOffsetPoint.DirectionOffset;
import com.dudko.blazinghot.content.block.shape.Shapes;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.util.DyeUtil;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModernLampHalfPanelBlock extends AbstractModernLampPanel implements SimpleWaterloggedBlock {

	public static final BooleanProperty HORIZONTAL = BooleanProperty.create("horizontal");

	private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

	public ModernLampHalfPanelBlock(Properties properties, DyeColor color) {
		super(properties, color);
		registerDefaultState(defaultBlockState().setValue(HORIZONTAL, true));
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

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(HORIZONTAL));
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		assert state != null;
		Direction facing = state.getValue(FACING);
		Vec2
				clickedPos =
				AbstractPoint.flatten3D(context.getClickLocation().subtract(context.getClickedPos().getCenter()),
						facing.getAxis());
		DirectionOffset offset = AbstractPoint.getNearest(DirectionOffsetPoint.fourPoints(), clickedPos).offset;
		return state.setValue(HORIZONTAL, offset.horizontal);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		boolean horizontal = state.getValue(HORIZONTAL);
		VoxelShape panel = Shapes.halfPanel(horizontal);
		VoxelShape halfPanelBase = Shapes.halfPanelBase(horizontal);
		return Shapes.shape(halfPanelBase).add(panel).forDirectional().get(state.getValue(FACING));
	}

	private static class PlacementHelper extends PoleHelper<Direction> {

		public PlacementHelper() {
			super(state -> BlazingBlocks.MODERN_LAMP_HALF_PANELS.contains(state.getBlock()), state -> {
				boolean horizontal = state.getValue(HORIZONTAL);
				Direction facing = state.getValue(FACING);
				if (facing.getAxis().isVertical()) return horizontal ? Axis.X : Axis.Z;
				return horizontal ? facing.getCounterClockWise().getAxis() : Axis.Y;
			}, FACING);
		}

		@Override
		public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
			PlacementOffset placementOffset = super.getOffset(player, world, state, pos, ray);
			if (placementOffset.equals(PlacementOffset.fail())) return placementOffset;

			return placementOffset.withTransform(newState -> newState
					.setValue(HORIZONTAL, state.getValue(HORIZONTAL))
					.setValue(FACING, state.getValue(FACING)));
		}

		@Override
		public Predicate<ItemStack> getItemPredicate() {
			return stack -> DyeUtil.isIn(BlazingBlocks.MODERN_LAMP_HALF_PANELS, stack);
		}

	}

}
