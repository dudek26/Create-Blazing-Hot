package com.dudko.blazinghot.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.content.kinetics.deployer.BeltDeployerCallbacks;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;

import net.minecraft.world.item.ItemStack;

@Mixin(value = BeltDeployerCallbacks.class, remap = false)
public abstract class BeltDeployerCallbacksMixin {

	@Inject(method = "awardAdvancements", at = @At(value = "RETURN", ordinal = 0))
	private static void blazinghot$award(DeployerBlockEntity blockEntity, ItemStack created, CallbackInfo ci) {

		BlazingAdvancement advancement;
		if (BlazingBlocks.BLAZE_CASING.isIn(created)) {
			advancement = BlazingAdvancements.BLAZE_CASING;
		}
		else if (BlazingBlocks.STURDY_CASING.isIn(created)) {
			advancement = BlazingAdvancements.STURDY_CASING;
		}
		else return;

		((IAdvancementBehaviour) blockEntity).blazinghot$award(advancement);

	}

}
