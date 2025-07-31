package com.dudko.blazinghot.content.casting.casting_depot.forge;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour.State;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.createmod.catnip.platform.ForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;

public class CastingDepotRendererImpl {

	public static void renderFluid(CastingDepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
		SmartFluidTankBehaviour tank = be.getTank();
		if (tank == null) return;

		FluidStack tankFluidStack = tank.getPrimaryTank().getRenderedFluid();
		SpoutCastingBehaviour spoutBehaviour = be.getBehaviour(SpoutCastingBehaviour.TYPE);

		if (be.getHeldItem().isEmpty()) return;
		if (spoutBehaviour.getState() == State.NONE) return;

		int processingDuration = spoutBehaviour.getRecipeProcessingDuration();
		if (processingDuration < 10) return;

		FluidStack
				fluidStack =
				spoutBehaviour.getState() == State.COOLING ?
				tankFluidStack :
				new FluidStack(be.getVisualFluid(), 1, tankFluidStack.getTag());

		int processingTicks = spoutBehaviour.getProcessingTicks() - 1;
		float processingPT = processingTicks + partialTicks;
		float level = 1;
		if (spoutBehaviour.getState() == State.FILLING) {
			level = Mth.clamp((processingPT - 5) / (processingDuration - 5), 0, 1);
		}
		if (!fluidStack.isEmpty() && level != 0) {
			float xMin = 1 / 16f;
			float xMax = 15 / 16f;
			final float yMin = 11 / 16f;
			final float yMax = yMin + 0.7f / 16 * level;
			final float zMin = 1 / 16f;
			final float zMax = 15 / 16f;
			ms.pushPose();
			ForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack,
					xMin,
					yMin,
					zMin,
					xMax,
					yMax,
					zMax,
					buffer,
					ms,
					light,
					false,
					false);
			ms.popPose();
		}
	}
}
