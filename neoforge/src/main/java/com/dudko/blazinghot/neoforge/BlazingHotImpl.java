package com.dudko.blazinghot.neoforge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.mechanical_arm.BlazingArmInteractionPointTypes;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.advancement.BlazingTriggers;
import com.dudko.blazinghot.foundation.multiloader.Env;
import com.dudko.blazinghot.foundation.recipe.neoforge.BlazingRecipeTypeImpl;
import com.dudko.blazinghot.registry.neoforge.BlazingConfigsImpl;
import com.dudko.blazinghot.registry.neoforge.BlazingCreativeTabsImpl;
import com.dudko.blazinghot.registry.neoforge.BlazingFluidsImpl;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(BlazingHot.ID)
@EventBusSubscriber
public class BlazingHotImpl {
	static IEventBus modEventBus;

	public BlazingHotImpl(IEventBus eventBus, ModContainer modContainer) {
		// registrate must be given the mod event bus on forge before registration
		ModLoadingContext modLoadingContext = ModLoadingContext.get();
		modEventBus = eventBus;

		BlazingCreativeTabsImpl.register(modEventBus);
		BlazingHot.init();

		modEventBus.addListener(EventPriority.LOWEST, BlazingHotDataNeoForge::gatherData);
		BlazingConfigsImpl.register(modLoadingContext, modContainer);
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
		BlazingRecipeTypeImpl.registerAll(modEventBus);
		BlazingHot.registrate().registerEventListeners(modEventBus);

		modEventBus.addListener(BlazingHotImpl::init);
		modEventBus.addListener(BlazingHotImpl::onRegister);
	}


}
