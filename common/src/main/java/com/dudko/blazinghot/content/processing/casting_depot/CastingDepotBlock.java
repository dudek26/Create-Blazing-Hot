package com.dudko.blazinghot.content.processing.casting_depot;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.block.shape.Shapes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.depot.SharedDepotBlockMethods;
import com.simibubi.create.foundation.block.IBE;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CastingDepotBlock extends Block implements IWrenchable, IBE<CastingDepotBlockEntity> {
	public CastingDepotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<CastingDepotBlockEntity> getBlockEntityClass() {
		return CastingDepotBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CastingDepotBlockEntity> getBlockEntityType() {
		return platformedBlockEntity();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext
				&& ((EntityCollisionContext) context).getEntity() instanceof Player)
			return AllShapes.CASING_12PX.get(Direction.UP);

		return Shapes.CASTING_DEPOT_SHAPE;
	}

	@ExpectPlatform
	public static <T extends CastingDepotBlockEntity> BlockEntityType<T> platformedBlockEntity() {
		throw new AssertionError();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
		return SharedDepotBlockMethods.onUse(state, world, pos, player, hand, ray);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		IBE.onRemove(state, worldIn, pos, newState);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	}
}
