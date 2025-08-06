package com.dudko.blazinghot.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = ManualApplicationRecipe.class, remap = false)
public abstract class ManualApplicationRecipeMixin {

	@Inject(method = "awardAdvancements", at = @At(value = "RETURN", ordinal = 0))
	private static void blazinghot$award(Player player, BlockState placed, CallbackInfo ci) {

		BlazingAdvancement advancement;
		if (BlazingBlocks.BLAZE_CASING.has(placed)) {
			advancement = BlazingAdvancements.BLAZE_CASING;
		}
		else if (BlazingBlocks.STURDY_CASING.has(placed)) {
			advancement = BlazingAdvancements.STURDY_CASING;
		}
		else return;

		advancement.awardTo(player);

	}

}
