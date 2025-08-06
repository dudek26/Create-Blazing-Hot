package com.dudko.blazinghot.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class BlazingFluids {

	public static void register() {
		platformRegister();
	}

	@ExpectPlatform
	public static void platformRegister() {
		throw new AssertionError();
	}

	public static boolean isWater(Fluid fluid) {
		return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
	}

	public static boolean isLava(Fluid fluid) {
		return fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA;
	}

}
