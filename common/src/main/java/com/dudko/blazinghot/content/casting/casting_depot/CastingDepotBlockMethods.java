package com.dudko.blazinghot.content.casting.casting_depot;

import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class CastingDepotBlockMethods {

	@ExpectPlatform
	public static InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult ray) {
		throw new AssertionError();
	}

	public static CastingDepotBehaviour getDepotBehaviour(BlockGetter worldIn, BlockPos pos) {
		return BlockEntityBehaviour.get(worldIn, pos, CastingDepotBehaviour.TYPE);
	}

	public static SpoutCastingBehaviour getSpoutingBehaviour(BlockGetter worldIn, BlockPos pos) {
		return BlockEntityBehaviour.get(worldIn, pos, SpoutCastingBehaviour.TYPE);
	}

	@ExpectPlatform
	public static ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
		throw new AssertionError();
	}

	public static void onLanded(BlockGetter worldIn, Entity entityIn) {
		ItemStack asItem = ItemHelper.fromItemEntity(entityIn);
		if (asItem.isEmpty()) return;
		if (entityIn.level().isClientSide) return;

		BlockPos pos = entityIn.blockPosition();
		CastingDepotBehaviour behaviour = getDepotBehaviour(worldIn, pos);
		SpoutCastingBehaviour castingBehaviour = getSpoutingBehaviour(worldIn, pos);
		if (behaviour == null || castingBehaviour == null) return;
		if (castingBehaviour.getState() != SpoutCastingBehaviour.State.NONE) return;

		if (behaviour.getHeldItemStack() != ItemStack.EMPTY) return;

		Vec3 targetLocation = VecHelper.getCenterOf(pos).add(0, 5 / 16f, 0);
		if (!PackageEntity.centerPackage(entityIn, targetLocation)) return;

		ItemStack inserted = asItem.copyWithCount(1);
		behaviour.setHeldStack(inserted);
		asItem.shrink(1);

		behaviour.blockEntity.notifyUpdate();

		if (entityIn instanceof ItemEntity) ((ItemEntity) entityIn).setItem(asItem);
		if (asItem.isEmpty()) entityIn.discard();
	}
}
