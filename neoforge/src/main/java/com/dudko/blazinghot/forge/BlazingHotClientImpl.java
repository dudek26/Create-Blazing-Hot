package com.dudko.blazinghot.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.BlazingHotClient;
import com.dudko.blazinghot.registry.BlazingConfigs;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

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
