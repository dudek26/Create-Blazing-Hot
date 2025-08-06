package com.dudko.blazinghot.registry;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.CClient;
import com.dudko.blazinghot.config.CServer;
import com.dudko.blazinghot.config.CStress;
import com.simibubi.create.api.stress.BlockStressValues;

import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraft.client.gui.screens.Screen;

public class BlazingConfigs {

	public static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

	private static CClient client;
	private static CServer server;

	public static CClient client() {
		return client;
	}

	public static CServer server() {
		return server;
	}

	public static ConfigBase byType(ModConfig.Type type) {
		return CONFIGS.get(type);
	}

	private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
		Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
			T config = factory.get();
			config.registerAll(builder);
			return config;
		});

		T config = specPair.getLeft();
		config.specification = specPair.getRight();
		CONFIGS.put(side, config);
		return config;
	}

	public static void registerCommon() {
		client = register(CClient::new, ModConfig.Type.CLIENT);
		server = register(CServer::new, ModConfig.Type.SERVER);

		CStress stress = server().stressValues;
		BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
		BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);
	}

	public static void onLoad(ModConfig modConfig) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == modConfig.getSpec()) config.onLoad();
	}

	public static void onReload(ModConfig modConfig) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == modConfig.getSpec()) config.onReload();
	}

	public static BaseConfigScreen createConfigScreen(Screen parent) {
		BaseConfigScreen.setDefaultActionFor(BlazingHot.ID,
				(base) -> base
						.withSpecs(client().specification, null, server().specification)
						.withButtonLabels("Client Settings", "", "Gameplay Settings"));
		return new BaseConfigScreen(parent, BlazingHot.ID);
	}

}
