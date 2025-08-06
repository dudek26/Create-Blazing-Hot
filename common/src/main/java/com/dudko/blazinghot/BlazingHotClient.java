package com.dudko.blazinghot;

import com.dudko.blazinghot.gui.ponder.BlazingPonderPlugin;
import com.dudko.blazinghot.registry.BlazingPartialModels;

import net.createmod.ponder.foundation.PonderIndex;

public class BlazingHotClient {

	public static void init() {
		BlazingPartialModels.init();
		PonderIndex.addPlugin(new BlazingPonderPlugin());
	}

}
