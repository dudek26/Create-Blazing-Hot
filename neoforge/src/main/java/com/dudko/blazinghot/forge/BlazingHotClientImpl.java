package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.BlazingHotClient;
import com.dudko.blazinghot.registry.BlazingConfigs;

import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.neoforged.neoforge.eventbus.api.IEventBus;
import net.neoforged.neoforge.fml.ModContainer;
import net.neoforged.neoforge.fml.ModList;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class BlazingHotClientImpl {

	public static void initClient(IEventBus modEventBus) {
		BlazingHotClient.init();

		modEventBus.addListener(BlazingHotClientImpl::onLoadComplete);
	}

	public static void onLoadComplete(FMLLoadCompleteEvent event) {
		ModContainer
				container =
				ModList
						.get()
						.getModContainerById(BlazingHot.ID)
						.orElseThrow(() -> new IllegalStateException(
								"Create: Blazing Hot mod container missing on LoadComplete"));
		container.registerExtensionPoint(ConfigScreenFactory.class,
				() -> new ConfigScreenFactory((mc, screen) -> BlazingConfigs.createConfigScreen(screen)));
	}

}
