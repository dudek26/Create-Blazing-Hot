package com.dudko.blazinghot.content.casting.casting_depot;

import static com.dudko.blazinghot.util.DirectionUtil.HORIZONTAL_ANGLES;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CastingDepotRenderer extends SafeBlockEntityRenderer<CastingDepotBlockEntity> {

	public CastingDepotRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	protected void renderSafe(CastingDepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

		renderFluid(be, partialTicks, ms, buffer, light, overlay);

		ms.pushPose();
		renderItem(be.getLevel(),
				ms,
				buffer,
				light,
				overlay,
				be.getHeldItem(),
				be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));
		ms.popPose();
	}

	public static void renderItem(Level level, PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack, Direction direction) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		TransformStack msr = TransformStack.cast(ms);

		ms.translate(0.5, 23 / 32d, 0.5);
		ms.scale(14 / 16f, 1, 14 / 16f);
		if (!(itemStack.getItem() instanceof BlockItem)) msr.rotateX(90);
		msr.rotate(HORIZONTAL_ANGLES.get(direction), Direction.Axis.Z);

		itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, level, 0);
	}

	@ExpectPlatform
	public static void renderFluid(CastingDepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		throw new AssertionError();
	}

}
