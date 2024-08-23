package com.dudko.blazinghot.fabric;

import com.dudko.blazinghot.BlazingHotClient;

import net.fabricmc.api.ClientModInitializer;

public class BlazingHotClientImpl implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlazingHotClient.init();
	}
}
