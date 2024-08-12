package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHotClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class BlazingHotClientImpl {

    public static void init() {
        BlazingHotClient.init();
    }

}
