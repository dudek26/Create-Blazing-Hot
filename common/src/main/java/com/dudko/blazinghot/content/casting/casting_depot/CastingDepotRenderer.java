package com.dudko.blazinghot.content.casting.casting_depot;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CastingDepotRenderer extends SafeBlockEntityRenderer<CastingDepotBlockEntity> {

	public CastingDepotRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	protected void renderSafe(CastingDepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		renderItem(be.getLevel(), ms, buffer, light, overlay, be.getHeldItem());
	}

	public static void renderItem(Level level, PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		TransformStack msr = TransformStack.cast(ms);

		ms.translate(0.5, 23 / 32d, 0.5);
		ms.scale(14 / 16f, 1, 14 / 16f);
		msr.rotateX(90);

		itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, level, 0);
	}

}
