package com.dudko.blazinghot.foundation.mixin;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.MixerFuel;
import com.dudko.blazinghot.foundation.mixin_interfaces.IProcessingRecipeParams;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

@Mixin(value = ProcessingRecipeParams.class, remap = false)
public abstract class ProcessingRecipeParamsMixin implements IProcessingRecipeParams {

	@Unique
	@Nullable
	private MixerFuel fuel;

	@Override
	public Optional<MixerFuel> blazinghot$getMixerFuel() {
		return Optional.ofNullable(this.fuel);
	}

	@Override
	public void blazinghot$setMixerFuel(Optional<MixerFuel> fuel) {
		this.fuel = fuel.orElse(null);
	}

	@Inject(method = "codec", at = @At("RETURN"), cancellable = true)
	private static <P extends ProcessingRecipeParams> void blazinghot$codec(Supplier<P> factory,
																			CallbackInfoReturnable<MapCodec<P>> cir) {
		cir.setReturnValue(IProcessingRecipeParams.codec(cir.getReturnValue()));
	}
}
