package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlazingHot.ID)
@Mod.EventBusSubscriber
public class BlazingHotImpl {
    static IEventBus eventBus;

    public BlazingHotImpl() {
        // registrate must be given the mod event bus on forge before registration
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlazingHot.init();
    }

    public static void finalizeRegistrate() {
        BlazingHot.registrate().registerEventListeners(eventBus);
    }


}
