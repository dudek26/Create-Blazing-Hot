package com.dudko.blazinghot.events.fabric;

import com.dudko.blazinghot.content.fluid.fabric.FluidReactionsImpl;
import com.simibubi.create.api.event.PipeCollisionEvent;

public class BlazingEventsImpl {

	public static void register() {
		PipeCollisionEvent.SPILL.register(FluidReactionsImpl::handlePipeSpillCollisionFallback);
	}

}
