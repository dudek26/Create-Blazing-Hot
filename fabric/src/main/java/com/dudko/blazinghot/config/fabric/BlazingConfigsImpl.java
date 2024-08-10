package com.dudko.blazinghot.config.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.BlazingConfigs;
import com.simibubi.create.foundation.config.ConfigBase;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

public class BlazingConfigsImpl {

    public static void register() {
        BlazingConfigs.registerCommon();

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : BlazingConfigs.CONFIGS.entrySet())
            ForgeConfigRegistry.INSTANCE.register(BlazingHot.ID, pair.getKey(), pair.getValue().specification);

        ModConfigEvents.loading(BlazingHot.ID).register(BlazingConfigs::onLoad);
        ModConfigEvents.reloading(BlazingHot.ID).register(BlazingConfigs::onReload);
    }

}
