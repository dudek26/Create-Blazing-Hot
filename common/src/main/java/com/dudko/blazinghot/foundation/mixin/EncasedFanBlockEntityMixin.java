package com.dudko.blazinghot.foundation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.foundation.mixin_interfaces.IAirCurrent;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;

@Mixin(value = EncasedFanBlockEntity.class, remap = false)
public abstract class EncasedFanBlockEntityMixin {

	@Shadow
	public AirCurrent airCurrent;

	@Inject(method = "remove", at = @At("RETURN"))
	public void blazinghot$updateCastingDepots(CallbackInfo ci) {
		((IAirCurrent) airCurrent).blazinghot$clearCastingHandlers();
	}

}
