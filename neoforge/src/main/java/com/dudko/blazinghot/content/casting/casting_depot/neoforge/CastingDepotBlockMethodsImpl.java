package com.dudko.blazinghot.content.casting.casting_depot.neoforge;

import static com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockMethods.getDepotBehaviour;
import static com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockMethods.getSpoutingBehaviour;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockMethods;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * @see CastingDepotBlockMethods
 */
public class CastingDepotBlockMethodsImpl {

	public static ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
		if (ray.getDirection() != Direction.UP) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (world.isClientSide) return ItemInteractionResult.SUCCESS;

		CastingDepotBehaviourImpl behaviour = (CastingDepotBehaviourImpl) getDepotBehaviour(world, pos);
		SpoutCastingBehaviour castingBehaviour = getSpoutingBehaviour(world, pos);
		if (behaviour == null || castingBehaviour == null)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!behaviour.canAcceptItems.get()) return ItemInteractionResult.SUCCESS;

		boolean wasEmptyHanded = stack.isEmpty();
		boolean shouldntPlaceItem = AllBlocks.MECHANICAL_ARM.isIn(stack);
		boolean isCooling = castingBehaviour.getState() == SpoutCastingBehaviour.State.COOLING;
		boolean extracted = false;

		ItemStackHandler outputs = behaviour.processingOutputBuffer;
		for (int i = 0; i < outputs.getSlots(); i++) {
			ItemStack outputStack = outputs.extractItem(i, 64, false);
			if (!outputStack.isEmpty()) extracted = true;
			player.getInventory().placeItemBackInInventory(outputStack);
		}

		if (isCooling) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

		ItemStack mainItemStack = behaviour.getHeldItemStack();
		if (!mainItemStack.isEmpty() && !extracted) {
			if (!player.getItemInHand(hand).isEmpty()) return ItemInteractionResult.SUCCESS;
			player.getInventory().placeItemBackInInventory(mainItemStack);
			behaviour.removeHeldStack();
			world.playSound(null,
					pos,
					SoundEvents.ITEM_PICKUP,
					SoundSource.PLAYERS,
					.2f,
					1f + world.getRandom().nextFloat());
		}

		if (!wasEmptyHanded && !shouldntPlaceItem && !extracted) {
			behaviour.setHeldStack(stack.copyWithCount(1));
			ItemStack newStack = stack.copyWithCount(stack.getCount() - 1);
			player.setItemInHand(hand, newStack);
			AllSoundEvents.DEPOT_SLIDE.playOnServer(world, pos);
		}

		behaviour.blockEntity.notifyUpdate();
		return ItemInteractionResult.SUCCESS;
	}

}
