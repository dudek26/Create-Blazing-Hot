package com.dudko.blazinghot.gui.ponder.fabric;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.world.level.material.Fluid;

public class BlazingPonderScenesImpl {

	public static void setFluidInTank(SmartFluidTankBehaviour tank, Fluid fluid, long amount) {
		tank.getPrimaryHandler().setFluid(new FluidStack(fluid, amount));
	}

	public static void setFluidInTank(FluidTankBlockEntity tank, Fluid fluid, long amount) {
		tank.getTankInventory().setFluid(new FluidStack(fluid, amount));
	}
}
