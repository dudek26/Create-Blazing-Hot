package com.dudko.blazinghot.config.neoforge;

import java.util.Map;

import com.dudko.blazinghot.registry.BlazingConfigs;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber
public class BlazingConfigsImpl {
	public static void register(ModLoadingContext context) {
		BlazingConfigs.registerCommon();

		for (Map.Entry<ModConfig.Type, ConfigBase> pair : BlazingConfigs.CONFIGS.entrySet())
			context.registerConfig(pair.getKey(), pair.getValue().specification);
	}

	@SubscribeEvent
	public static void onLoad(ModConfigEvent.Loading event) {
		for (ConfigBase config : BlazingConfigs.CONFIGS.values())
			if (config.specification == event.getConfig().getSpec()) config.onLoad();
	}

	@SubscribeEvent
	public static void onReload(ModConfigEvent.Reloading event) {
		for (ConfigBase config : BlazingConfigs.CONFIGS.values())
			if (config.specification == event.getConfig().getSpec()) config.onReload();
	}
}
