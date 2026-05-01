package com.dudko.blazinghot.neoforge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.casting.casting_depot.neoforge.CastingDepotBlockEntityImpl;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.neoforge.BlazeMixerBlockEntityImpl;
import com.dudko.blazinghot.content.kinetics.mechanical_arm.BlazingArmInteractionPointTypes;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.advancement.BlazingTriggers;
import com.dudko.blazinghot.foundation.recipe.neoforge.BlazingRecipeTypeImpl;
import com.dudko.blazinghot.registry.neoforge.BlazingConfigsImpl;
import com.dudko.blazinghot.registry.neoforge.BlazingCreativeTabsImpl;
import com.dudko.blazinghot.registry.neoforge.BlazingDataMapsNeoForge;
import com.dudko.blazinghot.registry.neoforge.BlazingFluidsImpl;

import net.minecraft.core.registries.BuiltInRegistries;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

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
		modEventBus.addListener(EventPriority.HIGHEST, BlazingHotDataNeoForge::gatherDataHighPriority);

		BlazingConfigsImpl.register(modLoadingContext, modContainer);
	}

	public static void init(final FMLCommonSetupEvent event) {
		BlazingFluidsImpl.registerFluidInteractions();
	}

	public static void onRegister(final RegisterEvent event) {
		BlazingArmInteractionPointTypes.init();

		if (event.getRegistry() == BuiltInRegistries.TRIGGER_TYPES) {
			BlazingAdvancements.register();
			BlazingTriggers.register();
		}
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		BlazeMixerBlockEntityImpl.registerCapabilities(event);
		CastingDepotBlockEntityImpl.registerCapabilities(event);
	}

	@SubscribeEvent
	public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
		BlazingDataMapsNeoForge.register(event);
	}

	public static void finalizeRegistrate() {
		BlazingRecipeTypeImpl.registerAll(modEventBus);
		BlazingHot.registrate().registerEventListeners(modEventBus);

		modEventBus.addListener(BlazingHotImpl::init);
		modEventBus.addListener(BlazingHotImpl::onRegister);
	}


}
