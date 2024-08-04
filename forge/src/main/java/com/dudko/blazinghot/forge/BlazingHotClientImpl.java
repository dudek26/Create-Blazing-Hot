package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHotClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class BlazingHotClientImpl {

    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(BlazingHotClientImpl::init);
    }

    public static void init(final FMLClientSetupEvent event) {
        BlazingHotClient.init();
    }

}
