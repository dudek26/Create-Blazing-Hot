package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.forge.BlazingConfigsImpl;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.advancement.BlazingTriggers;
import com.dudko.blazinghot.multiloader.Env;
import com.dudko.blazinghot.registry.forge.BlazingCreativeTabsImpl;
import com.dudko.blazinghot.registry.forge.BlazingFluidsImpl;
import com.dudko.blazinghot.registry.forge.BlazingRecipeTypesImpl;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlazingHot.ID)
@Mod.EventBusSubscriber
public class BlazingHotImpl {
    static IEventBus modEventBus;

    public BlazingHotImpl() {
        // registrate must be given the mod event bus on forge before registration
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlazingCreativeTabsImpl.register(modEventBus);
        BlazingHot.init();
        BlazingConfigsImpl.register(ModLoadingContext.get());
        Env.CLIENT.runIfCurrent(() -> BlazingHotClientImpl::init);
    }

    public static void init(final FMLCommonSetupEvent event) {
        BlazingFluidsImpl.registerFluidInteractions();

        event.enqueueWork(() -> {
            BlazingAdvancements.register();
            BlazingTriggers.register();
        });
    }

    public static void finalizeRegistrate() {
        BlazingRecipeTypesImpl.platformRegister(modEventBus);
        BlazingHot.registrate().registerEventListeners(modEventBus);

        modEventBus.addListener(BlazingHotImpl::init);
    }


}
