package com.dudko.blazinghot.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancementBehaviour;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

@Mixin(value = DeployerBlockEntity.class, remap = false)
public abstract class DeployerBlockEntityMixin {

	@Inject(method = "addBehaviours", at = @At(value = "TAIL"))
	private void blazinghot$addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
		blazinghot$registerAwardables(behaviours, BlazingAdvancements.BLAZE_CASING);
	}

	@Unique
	public void blazinghot$registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements) {
		for (BlockEntityBehaviour behaviour : behaviours) {
			if (behaviour instanceof BlazingAdvancementBehaviour ab) {
				ab.add(advancements);
				return;
			}
		}
		behaviours.add(new BlazingAdvancementBehaviour((SmartBlockEntity) (Object) this, advancements));
	}
}
