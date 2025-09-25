package com.dudko.blazinghot.foundation.multiloader.neoforge;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.dudko.blazinghot.foundation.multiloader.Env;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;


public class EnvImpl {

	@Internal
	public static Env getCurrent() {
		return FMLEnvironment.dist == Dist.CLIENT ? Env.CLIENT : Env.SERVER;
	}
}
