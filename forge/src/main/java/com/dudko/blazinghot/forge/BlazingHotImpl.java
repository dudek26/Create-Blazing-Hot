package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.forge.BlazingFluidsImpl;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.equipment.potatoCannon.BuiltinPotatoProjectileTypes;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.AllTriggers;
import com.simibubi.create.foundation.utility.AttachedRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

    public static void init(final FMLCommonSetupEvent event) {
        BlazingFluidsImpl.registerFluidInteractions();
    }

    public static void finalizeRegistrate() {
        BlazingHot.registrate().registerEventListeners(eventBus);

        eventBus.addListener(BlazingHotImpl::init);
    }


}
