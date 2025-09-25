package com.dudko.blazinghot.neoforge;

import java.util.function.Supplier;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.BlazingHotClient;
import com.dudko.blazinghot.registry.BlazingConfigs;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(Dist.CLIENT)
public class BlazingHotClientImpl {

	public static void initClient(IEventBus modEventBus) {
		BlazingHotClient.init();

		modEventBus.addListener(BlazingHotClientImpl::onLoadComplete);
	}

	@SubscribeEvent
	public static void onLoadComplete(FMLLoadCompleteEvent event) {
		ModContainer
				container =
				ModList
						.get()
						.getModContainerById(BlazingHot.ID)
						.orElseThrow(() -> new IllegalStateException(
								"Create: Blazing Hot mod container missing on LoadComplete"));
		Supplier<IConfigScreenFactory>
				configScreen =
				() -> (mc, previousScreen) -> BlazingConfigs.createConfigScreen(previousScreen);
		container.registerExtensionPoint(IConfigScreenFactory.class, configScreen);
	}

}
