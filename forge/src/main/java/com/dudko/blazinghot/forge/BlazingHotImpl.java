package com.dudko.blazinghot.forge;

import org.apache.maven.artifact.versioning.ArtifactVersion;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.forge.BlazingConfigsImpl;
import com.dudko.blazinghot.content.kinetics.mechanicalArm.BlazingArmInteractionPointTypes;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.advancement.BlazingTriggers;
import com.dudko.blazinghot.data.conditions.forge.LegacyFluidCondition;
import com.dudko.blazinghot.multiloader.Env;
import com.dudko.blazinghot.registry.forge.BlazingCreativeTabsImpl;
import com.dudko.blazinghot.registry.forge.BlazingFluidsImpl;
import com.dudko.blazinghot.registry.forge.BlazingRecipeTypesImpl;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.ModMismatchEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(BlazingHot.ID)
@Mod.EventBusSubscriber
public class BlazingHotImpl {
	static IEventBus modEventBus;

	public BlazingHotImpl() {
		// registrate must be given the mod event bus on forge before registration
		modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		BlazingCreativeTabsImpl.register(modEventBus);
		BlazingHot.init();

		CraftingHelper.register(LegacyFluidCondition.Serializer.INSTANCE);

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


	public static void onModMismatch(ModMismatchEvent event) {
		ArtifactVersion version = event.getPreviousVersion("blazinghot");
		if (version == null) return;

		if (version.getMajorVersion() == 0 && (version.getMinorVersion() < 7 || (version.getMinorVersion() == 7
				&& version.getMinorVersion() < 1))) {
			BlazingHot.LOGGER.info("Legacy version detected ({})", version);
			BlazingHot.USE_LEGACY_FLUID_AMOUNTS = true;
		}
	}

	@SubscribeEvent
	public static void onServerStart(ServerAboutToStartEvent event) {
		if (BlazingHot.USE_LEGACY_FLUID_AMOUNTS) BlazingHot.LOGGER.info("Using legacy fluid amounts");
	}

	@SubscribeEvent
	public static void onServerStop(ServerStoppedEvent event) {
		BlazingHot.USE_LEGACY_FLUID_AMOUNTS = false;
	}

	public static void onRegister(final RegisterEvent event) {
		BlazingArmInteractionPointTypes.init();
	}

	public static void finalizeRegistrate() {
		BlazingRecipeTypesImpl.platformRegister(modEventBus);
		BlazingHot.registrate().registerEventListeners(modEventBus);

		modEventBus.addListener(BlazingHotImpl::init);
		modEventBus.addListener(BlazingHotImpl::onRegister);
		modEventBus.addListener(BlazingHotImpl::onModMismatch);
	}


}
