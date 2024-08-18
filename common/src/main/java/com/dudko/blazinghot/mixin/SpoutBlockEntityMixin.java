package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SpoutBlockEntity.class, remap = false)
public abstract class SpoutBlockEntityMixin {

    @Inject(method = "addBehaviours", at = @At("TAIL"))
    private void addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        ((IAdvancementBehaviour) this).blazinghot$registerAwardables(behaviours);
    }

}
