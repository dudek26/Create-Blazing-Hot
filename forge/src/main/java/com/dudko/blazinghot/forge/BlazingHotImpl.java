package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.forge.BlazingCreativeTabsImpl;
import com.dudko.blazinghot.registry.forge.BlazingFluidsImpl;
import com.dudko.blazinghot.registry.forge.BlazingRecipeTypesImpl;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
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
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BlazingHotClientImpl.onCtorClient(modEventBus));
    }

    public static void init(final FMLCommonSetupEvent event) {
        BlazingFluidsImpl.registerFluidInteractions();
    }

    public static void finalizeRegistrate() {
        BlazingRecipeTypesImpl.platformRegister(modEventBus);
        BlazingHot.registrate().registerEventListeners(modEventBus);

        modEventBus.addListener(BlazingHotImpl::init);
    }


}
