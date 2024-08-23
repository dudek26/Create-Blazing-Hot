package com.dudko.blazinghot.config.forge;

import java.util.Map;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.simibubi.create.foundation.config.ConfigBase;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
