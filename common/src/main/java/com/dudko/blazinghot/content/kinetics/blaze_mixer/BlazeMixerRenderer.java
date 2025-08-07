package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.registry.BlazingPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeMixerRenderer extends KineticBlockEntityRenderer<BlazeMixerBlockEntity> {

	public BlazeMixerRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public boolean shouldRenderOffScreen(BlazeMixerBlockEntity be) {
		return true;
	}

	@Override
	protected void renderSafe(BlazeMixerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

		if (VisualizationManager.supportsVisualization(be.getLevel())) return;

		BlockState blockState = be.getBlockState();

		VertexConsumer vb = buffer.getBuffer(RenderType.solid());

		SuperByteBuffer
				superBuffer =
				CachedBuffers.partial(BlazingPartialModels.SHAFTLESS_CRIMSON_COGWHEEL, blockState);
		standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);

		float renderedHeadOffset = be.getRenderedHeadOffset(partialTicks);
		float speed = be.getRenderedHeadRotationSpeed(partialTicks);
		float time = AnimationTickHolder.getRenderTime(be.getLevel());
		float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;

		SuperByteBuffer poleRender = CachedBuffers.partial(BlazingPartialModels.BLAZE_MIXER_POLE, blockState);
		poleRender.translate(0, -renderedHeadOffset, 0).light(light).renderInto(ms, vb);

		VertexConsumer vbCutout = buffer.getBuffer(RenderType.cutoutMipped());
		SuperByteBuffer headRender = CachedBuffers.partial(BlazingPartialModels.BLAZE_MIXER_HEAD, blockState);
		headRender
				.rotateCentered(angle, Direction.UP)
				.translate(0, -renderedHeadOffset, 0)
				.light(light)
				.renderInto(ms, vbCutout);
	}

}
