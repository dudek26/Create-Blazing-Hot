package com.dudko.blazinghot.content.fluid.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.api.event.PipeCollisionEvent;
import com.simibubi.create.foundation.fluid.FluidHelper;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * From {@link com.simibubi.create.content.fluids.FluidReactions}
 */
public class FluidReactionsImpl {

	public static void handlePipeSpillCollisionFallback(PipeCollisionEvent.Spill event) {
		Fluid pf = event.getPipeFluid();
		Fluid wf = event.getWorldFluid();

		FluidState pfState = pf.defaultFluidState();
		FluidState wfState = wf.defaultFluidState();

		if (FluidHelper.isTag(pf, FluidTags.WATER) && wf == BlazingFluidsImpl.NETHER_LAVA.getSource()) {
			event.setState(Blocks.OBSIDIAN.defaultBlockState());
		}
		else if (pf == Fluids.WATER && wf == BlazingFluidsImpl.NETHER_LAVA.get().getFlowing()) {
			event.setState(Blocks.COBBLESTONE.defaultBlockState());
		}
		else if (pf == BlazingFluidsImpl.NETHER_LAVA.getSource() && wf == Fluids.WATER) {
			event.setState(Blocks.STONE.defaultBlockState());
		}
		else if (pf == BlazingFluidsImpl.NETHER_LAVA.getSource() && wf == Fluids.FLOWING_WATER) {
			event.setState(Blocks.COBBLESTONE.defaultBlockState());
		}

		if (FluidHelper.hasBlockState(pf)) {
			if (BlazingFluidsImpl.MOLTEN_METALS.contains(pf) && wfState.is(FluidTags.WATER)) {
				BlockState
						lavaInteraction =
						BlazingFluidsImpl.getLavaInteraction(pfState,
								FluidHelper.convertToFlowing(wf).defaultFluidState());
				if (lavaInteraction != null) {
					event.setState(lavaInteraction);
				}
			}
			else if (BlazingFluidsImpl.MOLTEN_METALS.contains(wf) && pfState.is(FluidTags.WATER)) {
				BlockState
						lavaInteraction =
						BlazingFluidsImpl.getLavaInteraction(wfState,
								FluidHelper.convertToFlowing(pf).defaultFluidState());
				if (lavaInteraction != null) {
					event.setState(lavaInteraction);
				}
			}
		}
	}

}
