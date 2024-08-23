package com.dudko.blazinghot.multiloader.fabric;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.dudko.blazinghot.multiloader.Env;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class EnvImpl {
	@Internal
	public static Env getCurrent() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Env.CLIENT : Env.SERVER;
	}
}
