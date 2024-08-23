package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHotClient;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class BlazingHotClientImpl {

	public static void init() {
		BlazingHotClient.init();
	}

}
