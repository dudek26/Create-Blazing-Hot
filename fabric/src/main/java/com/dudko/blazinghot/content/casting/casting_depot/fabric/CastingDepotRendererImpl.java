package com.dudko.blazinghot.content.casting.casting_depot.fabric;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.fluid.FluidRenderer;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;

@SuppressWarnings("UnstableApiUsage")
public class CastingDepotRendererImpl {

	public static void renderFluid(CastingDepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		SmartFluidTankBehaviour behaviour = be.getBehaviour(SmartFluidTankBehaviour.TYPE);
		float totalUnits = behaviour.getPrimaryTank().getTotalUnits(partialTicks);
		if (totalUnits < 1) return;

		float fluidLevel = Mth.clamp(totalUnits / be.getCapacity(), 0, 1);

		fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));

		float xMin = 1 / 16f;
		float xMax = 15 / 16f;
		final float yMin = 11 / 16f;
		final float yMax = yMin + 0.9f / 16 * fluidLevel;
		final float zMin = 1 / 16f;
		final float zMax = 15 / 16f;

		for (TankSegment tankSegment : behaviour.getTanks()) {
			FluidStack renderedFluid = tankSegment.getRenderedFluid();
			if (renderedFluid.isEmpty()) continue;
			float units = tankSegment.getTotalUnits(partialTicks);
			if (units < 1) continue;

			FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, false);

			xMin = xMax;
		}
	}

}
