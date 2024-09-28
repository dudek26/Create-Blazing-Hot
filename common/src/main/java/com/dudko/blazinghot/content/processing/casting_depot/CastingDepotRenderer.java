package com.dudko.blazinghot.content.processing.casting_depot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CastingDepotRenderer extends SafeBlockEntityRenderer<CastingDepotBlockEntity> {

	public CastingDepotRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	protected void renderSafe(CastingDepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

	}
}
