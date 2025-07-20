package com.dudko.blazinghot.content.fluid.fabric;

import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.AllFluids;
import com.simibubi.create.api.event.PipeCollisionEvent;
import com.simibubi.create.foundation.fluid.FluidHelper;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * From {@link com.simibubi.create.content.fluids.FluidReactions}
 */
public class FluidReactionsImpl {

	public static void handlePipeFlowCollisionFallback(PipeCollisionEvent.Flow event) {
		Fluid f1 = event.getFirstFluid();
		Fluid f2 = event.getSecondFluid();

		FlowingFluid flowingLava = (FlowingFluid) BlazingFluidsImpl.NETHER_LAVA.get().getSource();

		if (f1 == Fluids.WATER && f2 == flowingLava || f2 == Fluids.WATER && f1 == flowingLava) {
			event.setState(Blocks.COBBLESTONE.defaultBlockState());
		} else if (f1 == flowingLava && FluidHelper.hasBlockState(f2)) {
			lavaInteraction(event, f2);
		} else if (f2 == flowingLava && FluidHelper.hasBlockState(f1)) {
			lavaInteraction(event, f1);
		}

		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(f1) && FluidHelper.convertToFlowing(f2).defaultFluidState().is(FluidTags.WATER))
			metalInteraction(event, f1, f2);
		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(f2) && FluidHelper.convertToFlowing(f1).defaultFluidState().is(FluidTags.WATER))
			metalInteraction(event, f2, f1);
	}

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

		if (!FluidHelper.hasBlockState(pf)) return;

		if (pf == BlazingFluidsImpl.NETHER_LAVA.getSource()) lavaInteraction(event, wf);
		else if (wf == BlazingFluidsImpl.NETHER_LAVA.get().getFlowing()) lavaInteraction(event, pf);

		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(pf) && wfState.is(FluidTags.WATER))
			metalInteraction(event, pf, wf);

		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(wf) && pfState.is(FluidTags.WATER))
			metalInteraction(event, wf, pf);

	}

	private static void metalInteraction(PipeCollisionEvent event, Fluid metal, Fluid waterLike) {
		BlockState
				metalInteraction =
				BlazingFluidsImpl.getFluidInteraction(metal.defaultFluidState(),
						FluidHelper.convertToFlowing(waterLike).defaultFluidState());
		if (metalInteraction != null) {
			event.setState(metalInteraction);
		}
		else lavaInteraction(event, waterLike);
	}

	private static void lavaInteraction(PipeCollisionEvent event, Fluid fluid) {
		BlockState
				lavaInteraction =
				AllFluids.getLavaInteraction(FluidHelper.convertToFlowing(fluid).defaultFluidState());
		if (lavaInteraction != null) {
			event.setState(lavaInteraction);
		}
	}

	public static void registerEvents() {
		PipeCollisionEvent.FLOW.register(FluidReactionsImpl::handlePipeFlowCollisionFallback);
		PipeCollisionEvent.SPILL.register(FluidReactionsImpl::handlePipeSpillCollisionFallback);
	}

}
