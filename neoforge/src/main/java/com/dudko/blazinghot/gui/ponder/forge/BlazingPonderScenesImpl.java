package com.dudko.blazinghot.gui.ponder.forge;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BlazingPonderScenesImpl {

	public static void setFluidInTank(SmartFluidTankBehaviour tank, Fluid fluid, long amount) {
		tank.getPrimaryHandler().setFluid(new FluidStack(fluid, (int) amount));
	}

	public static void setFluidInTank(FluidTankBlockEntity tank, Fluid fluid, long amount) {
		tank.getTankInventory().setFluid(new FluidStack(fluid, (int) amount));
	}
}
