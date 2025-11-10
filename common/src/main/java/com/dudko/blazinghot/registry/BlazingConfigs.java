package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.CClient;
import com.dudko.blazinghot.config.CServer;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

public class BlazingConfigs {

	protected static CClient client;
	protected static CServer server;

	public static CClient client() {
		return client;
	}

	public static CServer server() {
		return server;
	}

	@Environment(EnvType.CLIENT)
	public static BaseConfigScreen createConfigScreen(Screen parent) {
		BaseConfigScreen.setDefaultActionFor(BlazingHot.ID,
				(base) -> base
						.withSpecs(client().specification, null, server().specification)
						.withButtonLabels("Client Settings", "", "Gameplay Settings"));
		return new BaseConfigScreen(parent, BlazingHot.ID);
	}

}
