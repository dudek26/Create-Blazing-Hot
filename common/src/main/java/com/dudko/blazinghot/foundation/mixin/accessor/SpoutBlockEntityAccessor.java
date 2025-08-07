package com.dudko.blazinghot.foundation.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

@Mixin(value = SpoutBlockEntity.class, remap = false)
public interface SpoutBlockEntityAccessor {

	@Accessor
	SmartFluidTankBehaviour getTank();

}
