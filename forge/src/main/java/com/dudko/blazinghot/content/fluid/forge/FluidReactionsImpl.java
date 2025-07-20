package com.dudko.blazinghot.content.fluid.forge;

import static com.dudko.blazinghot.registry.forge.BlazingFluidsImpl.NETHER_LAVA;

import com.dudko.blazinghot.registry.forge.BlazingFluidsImpl;
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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * From {@link com.simibubi.create.content.fluids.FluidReactions}
 */
@EventBusSubscriber
public class FluidReactionsImpl {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void handlePipeFlowCollisionFallback(PipeCollisionEvent.Flow event) {
		Fluid f1 = event.getFirstFluid();
		Fluid f2 = event.getSecondFluid();

		FlowingFluid netherLava = NETHER_LAVA.getSource();

		if (f1 == Fluids.WATER && f2 == netherLava || f2 == Fluids.WATER && f1 == netherLava) {
			event.setState(Blocks.COBBLESTONE.defaultBlockState());
		}
		else if (f1 == netherLava && FluidHelper.hasBlockState(f2)) {
			BlockState
					lavaInteraction =
					AllFluids.getLavaInteraction(FluidHelper.convertToFlowing(f2).defaultFluidState());
			if (lavaInteraction != null) {
				event.setState(lavaInteraction);
			}
		}
		else if (f2 == netherLava && FluidHelper.hasBlockState(f1)) {
			BlockState
					lavaInteraction =
					AllFluids.getLavaInteraction(FluidHelper.convertToFlowing(f1).defaultFluidState());
			if (lavaInteraction != null) {
				event.setState(lavaInteraction);
			}
		}

		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(f1) && f2 == Fluids.WATER) metalInteraction(event, f1, f2);
		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(f2) && f1 == Fluids.WATER) metalInteraction(event, f2, f1);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void handlePipeSpillCollisionFallback(PipeCollisionEvent.Spill event) {
		Fluid pf = event.getPipeFluid();
		Fluid wf = event.getWorldFluid();

		FluidState pfState = pf.defaultFluidState();
		FluidState wfState = wf.defaultFluidState();

		if (FluidHelper.isTag(pf, FluidTags.WATER) && wf == NETHER_LAVA.getSource()) {
			event.setState(Blocks.OBSIDIAN.defaultBlockState());
		}
		else if (pf == Fluids.WATER && wf == NETHER_LAVA.get().getFlowing()) {
			event.setState(Blocks.COBBLESTONE.defaultBlockState());
		}
		else if (pf == NETHER_LAVA.getSource() && wf == Fluids.WATER) {
			event.setState(Blocks.STONE.defaultBlockState());
		}
		else if (pf == NETHER_LAVA.getSource() && wf == Fluids.FLOWING_WATER) {
			event.setState(Blocks.COBBLESTONE.defaultBlockState());
		}

		if (pf == NETHER_LAVA.getSource()) lavaInteraction(event, wf);
		else if (wf == NETHER_LAVA.get().getFlowing() && FluidHelper.hasBlockState(pf)) lavaInteraction(event, pf);

		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(pf) && FluidHelper.isTag(wf, FluidTags.WATER)) {
			metalInteraction(event, pf, wf);
		}

		else if (BlazingFluidsImpl.MOLTEN_METALS.contains(wf) && FluidHelper.isTag(pf, FluidTags.WATER)) {
			metalInteraction(event, wf, pf);
		}
	}

	private static void metalInteraction(PipeCollisionEvent event, Fluid metal, Fluid waterLike) {
		BlockState
				metalInteraction =
				BlazingFluidsImpl.getFluidInteraction(metal.defaultFluidState(), waterLike.defaultFluidState());
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

}
