package com.dudko.blazinghot.config.fabric;

import java.util.Map;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingConfigs;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.fml.config.ModConfig;

public class BlazingConfigsImpl {

	public static void register() {
		BlazingConfigs.registerCommon();

		for (Map.Entry<ModConfig.Type, ConfigBase> pair : BlazingConfigs.CONFIGS.entrySet())
			ForgeConfigRegistry.INSTANCE.register(BlazingHot.ID, pair.getKey(), pair.getValue().specification);

		ModConfigEvents.loading(BlazingHot.ID).register(BlazingConfigs::onLoad);
		ModConfigEvents.reloading(BlazingHot.ID).register(BlazingConfigs::onReload);
	}

}
