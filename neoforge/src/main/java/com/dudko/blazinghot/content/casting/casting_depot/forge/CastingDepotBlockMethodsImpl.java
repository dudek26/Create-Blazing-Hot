package com.dudko.blazinghot.content.casting.casting_depot.forge;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockMethods;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CastingDepotBlockMethodsImpl {

	public static InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult ray) {
		if (ray.getDirection() != Direction.UP) return InteractionResult.PASS;
		if (world.isClientSide) return InteractionResult.SUCCESS;

		CastingDepotBehaviourImpl
				behaviour =
				(CastingDepotBehaviourImpl) CastingDepotBlockMethods.getDepotBehaviour(world, pos);
		SpoutCastingBehaviour castingBehaviour = CastingDepotBlockMethods.getSpoutingBehaviour(world, pos);
		if (behaviour == null || castingBehaviour == null) return InteractionResult.PASS;

		ItemStackHandler outputs = behaviour.processingOutputBuffer;
		for (int i = 0; i < outputs.getSlots(); i++) {
			ItemStack outputStack = outputs.extractItem(i, 64, false);
			if (!outputStack.isEmpty()) {
				player.getInventory().placeItemBackInInventory(outputStack);
				return InteractionResult.SUCCESS;
			}
		}

		if (castingBehaviour.getState() == SpoutCastingBehaviour.State.COOLING) return InteractionResult.PASS;

		ItemStack mainItemStack = behaviour.getHeldItemStack();
		if (!mainItemStack.isEmpty()) {
			player.getInventory().placeItemBackInInventory(mainItemStack);
			behaviour.removeHeldStack();
			world.playSound(null,
					pos,
					SoundEvents.ITEM_PICKUP,
					SoundSource.PLAYERS,
					.2f,
					1f + world.getRandom().nextFloat());
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
