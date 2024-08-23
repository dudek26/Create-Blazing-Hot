package com.dudko.blazinghot.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.data.advancement.BlazingAdvancementBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = SpoutBlock.class)
public class SpoutBlockMixin {

	@Inject(method = "setPlacedBy", at = @At("TAIL"))
	private void blazinghot$setBlazingAdvancementsOwner(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack, CallbackInfo ci) {
		BlazingAdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
	}

}
