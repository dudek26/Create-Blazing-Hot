package com.dudko.blazinghot.content.casting.casting_depot.forge;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

public class CastingDepotBlockMethodsImpl {

	protected static CastingDepotBehaviourImpl get(BlockGetter worldIn, BlockPos pos) {
		return (CastingDepotBehaviourImpl) BlockEntityBehaviour.get(worldIn, pos, CastingDepotBehaviour.INPUT);
	}

	public static InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
		if (ray.getDirection() != Direction.UP) return InteractionResult.PASS;
		if (world.isClientSide) return InteractionResult.SUCCESS;

		CastingDepotBehaviourImpl behaviour = get(world, pos);
		if (behaviour == null) return InteractionResult.PASS;
		if (!behaviour.canAcceptItems.get()) return InteractionResult.SUCCESS;

		ItemStack heldItem = player.getItemInHand(hand);
		boolean wasEmptyHanded = heldItem.isEmpty();
		boolean shouldntPlaceItem = AllBlocks.MECHANICAL_ARM.isIn(heldItem);

		ItemStack mainItemStack = behaviour.getHeldItemStack();
		if (!mainItemStack.isEmpty()) {
			if (!player.getItemInHand(hand).isEmpty()) return InteractionResult.SUCCESS;
			player.getInventory().placeItemBackInInventory(mainItemStack);
			behaviour.removeHeldStack();
			world.playSound(null,
					pos,
					SoundEvents.ITEM_PICKUP,
					SoundSource.PLAYERS,
					.2f,
					1f + world.getRandom().nextFloat());
		}
		ItemStackHandler outputs = behaviour.processingOutputBuffer;
		for (int i = 0; i < outputs.getSlots(); i++)
			player.getInventory().placeItemBackInInventory(outputs.extractItem(i, 64, false));

		if (!wasEmptyHanded && !shouldntPlaceItem) {
			behaviour.setHeldStack(heldItem.copyWithCount(1));
			ItemStack newHeldItem = heldItem.copyWithCount(heldItem.getCount() - 1);
			player.setItemInHand(hand, newHeldItem);
			AllSoundEvents.DEPOT_SLIDE.playOnServer(world, pos);
		}

		behaviour.blockEntity.notifyUpdate();
		return InteractionResult.SUCCESS;
	}

	public static void onLanded(BlockGetter worldIn, Entity entityIn) {
		ItemStack asItem = ItemHelper.fromItemEntity(entityIn);
		if (asItem.isEmpty()) return;
		if (entityIn.level().isClientSide) return;

		BlockPos pos = entityIn.blockPosition();
		CastingDepotBehaviourImpl behaviour = get(worldIn, pos);
		if (behaviour == null) return;

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
