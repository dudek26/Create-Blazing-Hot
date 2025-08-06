package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.forge.BlazingConfigsImpl;
import com.dudko.blazinghot.content.kinetics.mechanicalArm.BlazingArmInteractionPointTypes;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.advancement.BlazingTriggers;
import com.dudko.blazinghot.multiloader.Env;
import com.dudko.blazinghot.registry.forge.BlazingCreativeTabsImpl;
import com.dudko.blazinghot.registry.forge.BlazingFluidsImpl;
import com.dudko.blazinghot.registry.forge.BlazingRecipeTypesImpl;

import net.neoforged.neoforge.eventbus.api.EventPriority;
import net.neoforged.neoforge.eventbus.api.IEventBus;
import net.neoforged.neoforge.fml.ModLoadingContext;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(BlazingHot.ID)
@Mod.EventBusSubscriber
public class BlazingHotImpl {
	static IEventBus modEventBus;

	public BlazingHotImpl() {
		// registrate must be given the mod event bus on forge before registration
		modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		BlazingCreativeTabsImpl.register(modEventBus);
		BlazingHot.init();

		modEventBus.addListener(EventPriority.LOWEST, BlazingHotDataForge::gatherData);
		BlazingConfigsImpl.register(ModLoadingContext.get());
		Env.CLIENT.runIfCurrent(() -> () -> BlazingHotClientImpl.initClient(modEventBus));
	}

	public static void init(final FMLCommonSetupEvent event) {
		BlazingFluidsImpl.registerFluidInteractions();

		event.enqueueWork(() -> {
			BlazingAdvancements.register();
			BlazingTriggers.register();
		});
	}

	public static void onRegister(final RegisterEvent event) {
		BlazingArmInteractionPointTypes.init();
	}

	public static void finalizeRegistrate() {
		BlazingRecipeTypesImpl.platformRegister(modEventBus);
		BlazingHot.registrate().registerEventListeners(modEventBus);

		modEventBus.addListener(BlazingHotImpl::init);
		modEventBus.addListener(BlazingHotImpl::onRegister);
	}


}
