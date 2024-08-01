package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.ExampleBlocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlazingHot.MOD_ID)
@Mod.EventBusSubscriber
public class ExampleModForge {
    static IEventBus eventBus;

    public ExampleModForge() {
        // registrate must be given the mod event bus on forge before registration
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ExampleBlocks.REGISTRATE.registerEventListeners(eventBus);
        BlazingHot.init();
    }


}
