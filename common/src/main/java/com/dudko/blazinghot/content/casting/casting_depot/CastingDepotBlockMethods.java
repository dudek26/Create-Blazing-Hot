package com.dudko.blazinghot.content.casting.casting_depot;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CastingDepotBlockMethods {

	@ExpectPlatform
	public static InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void onLanded(BlockGetter worldIn, Entity entityIn) {
		throw new AssertionError();
	}
}
