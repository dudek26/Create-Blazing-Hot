package com.dudko.blazinghot.compat.neoforge;

import com.dudko.blazinghot.compat.Mods;

import net.neoforged.fml.ModList;

public class ModsImpl {

	public static boolean isModLoaded(Mods mod) {
		return ModList.get().isLoaded(mod.id);
	}

}
