package com.dudko.blazinghot.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.data.advancement.BlazingAdvancementBehaviour;
import com.simibubi.create.content.kinetics.base.KineticBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = KineticBlock.class)
public class KineticBlockMixin {

	@Inject(method = "setPlacedBy", at = @At("HEAD"))
	private void blazinghot$setBlazingAdvancementsOwner(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
		BlazingAdvancementBehaviour.setPlacedBy(worldIn, pos, placer);
	}

}
