package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.mixin.accessor.BasinOperatingBlockEntityAccessor;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MechanicalMixerBlockEntity.class)
public abstract class MechanicalMixerBlockEntityMixin {

    @ModifyArg(method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(III)I"),
            index = 2,
            remap = false)
    public int blazinghot$extendDuration(int max) {
        ResourceLocation blazinghot$id = ((BasinOperatingBlockEntityAccessor) this).getCurrentRecipe().getId();
        BlazingHot.LOGGER.info("Attempting to extend duration of {} to {} ", blazinghot$id.getPath(), max);
        if (blazinghot$id.getPath().startsWith("mixing/melting")) {
            BlazingHot.LOGGER.info("Extended duration of {} to {} ", blazinghot$id.getPath(), max);
            return max * 16;
        }
        else return max;
    }

}
