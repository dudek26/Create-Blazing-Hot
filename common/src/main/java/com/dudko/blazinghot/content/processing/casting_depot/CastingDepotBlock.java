package com.dudko.blazinghot.content.processing.casting_depot;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.block.shape.Shapes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
}
