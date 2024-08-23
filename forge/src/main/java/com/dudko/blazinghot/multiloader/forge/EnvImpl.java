package com.dudko.blazinghot.multiloader.forge;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.dudko.blazinghot.multiloader.Env;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class EnvImpl {

	@Internal
	public static Env getCurrent() {
		return FMLEnvironment.dist == Dist.CLIENT ? Env.CLIENT : Env.SERVER;
	}
}
