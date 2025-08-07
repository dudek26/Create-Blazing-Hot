package com.dudko.blazinghot.foundation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = AssemblyOperatorBlockItem.class, remap = false)
public abstract class AssemblyOperatorBlockItemMixin {

	@Inject(method = "operatesOn", at = @At("TAIL"), cancellable = true)
	public void blazinghot$operateOnCastingBasin(LevelReader world, BlockPos pos, BlockState placedOnState, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			cir.setReturnValue(BlazingBlocks.CASTING_DEPOT.has(placedOnState));
		}
	}
}
